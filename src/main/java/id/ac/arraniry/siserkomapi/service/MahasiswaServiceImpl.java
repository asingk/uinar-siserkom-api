package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.*;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import id.ac.arraniry.siserkomapi.entity.MataKuliahMahasiswa;
import id.ac.arraniry.siserkomapi.repository.MahasiswaRepo;
import id.ac.arraniry.siserkomapi.repository.MataKuliahRepo;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MahasiswaServiceImpl implements MahasiswaService {
    private final MahasiswaRepo mahasiswaRepo;
    private final Environment environment;
    private final MataKuliahRepo mataKuliahRepo;

    RestClient restClient = RestClient.create();

    public MahasiswaServiceImpl(MahasiswaRepo mahasiswaRepo, Environment environment, MataKuliahRepo mataKuliahRepo) {
        this.mahasiswaRepo = mahasiswaRepo;
        this.environment = environment;
        this.mataKuliahRepo = mataKuliahRepo;
    }

    @Override
    public Optional<Mahasiswa> findById(String id) {
        return mahasiswaRepo.findById(id);
    }

    @Override
    public Mahasiswa save(Mahasiswa mahasiswa) {
        return mahasiswaRepo.save(mahasiswa);
    }

    @Override
    public Page<Mahasiswa> findByIdOrNama(String searchString, Pageable pageable) {
        return mahasiswaRepo.findByIdStartsWithOrNamaContainingIgnoreCase(searchString, searchString, pageable);
    }

    @Override
    public Mahasiswa sync(String nim) {
        var mahasiswaSevimaResp = restClient.get()
                .uri(GlobalConstants.SIAKAD_API_URL + "/mahasiswa/" + nim)
                .header("X-App-Key", environment.getProperty("env.data.app-key"))
                .header("X-Secret-Key", environment.getProperty("env.data.secret-key"))
                .retrieve()
                .body(MahasiswaSevimaResp.class);
        var mhs = new Mahasiswa();
        assert mahasiswaSevimaResp != null;
        mhs.setId(mahasiswaSevimaResp.getAttributes().getNim());
        mhs.setNama(mahasiswaSevimaResp.getAttributes().getNama());
        mhs.setProdi(mahasiswaSevimaResp.getAttributes().getProgram_studi());
        mhs.setIsLulusMatkul(false);
        mhs.setCreatedAt(LocalDateTime.now());
        return mahasiswaRepo.save(mhs);
    }

    @Override
    @Transactional
    public List<MataKuliahMahasiswa> syncMataKuliah(String nim) {
//        System.out.println(mahasiswa.getMataKuliah().size());
        var matkulMhs = cekNilaiMataKuliah(nim);
        var mahasiswa = mahasiswaRepo.findById(nim).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa tidak ditemukan!"));
        var matKulMhsList = new HashSet<MataKuliahMahasiswa>();
        matkulMhs.forEach(row -> {
            var matKulMhs = new MataKuliahMahasiswa();
            matKulMhs.setMahasiswa(mahasiswa);
            var matkulOpt = mataKuliahRepo.findByIdAndIsDisabled(row.getKode_mata_kuliah(), false);
            matkulOpt.ifPresent(matKulMhs::setMataKuliah);
            matKulMhs.setNilai(row.getNilai_huruf());
            var listNilai = Arrays.asList(GlobalConstants.NILAI_MK_LULUS);
            matKulMhs.setIsLulus(listNilai.contains(row.getNilai_huruf()));
            matKulMhs.setCreatedAt(LocalDateTime.now());
            matKulMhsList.add(matKulMhs);
            if(matKulMhs.getIsLulus()) mahasiswa.setIsLulusMatkul(true);
        });
        if(!matkulMhs.isEmpty()) {
            if(null == mahasiswa.getMataKuliah()) {
                mahasiswa.setMataKuliah(matKulMhsList);
            } else {
                mahasiswa.getMataKuliah().addAll(matKulMhsList);
            }
            mahasiswa.setUpdatedAt(LocalDateTime.now());
        }
        return mahasiswaRepo.save(mahasiswa).getMataKuliah().stream().toList();
    }

    private List<KhsSevimaRespDataAttributes> cekNilaiMataKuliah(String nim) {
        var khsSevimaResp = restClient.get()
                .uri(GlobalConstants.SIAKAD_API_URL + "/mahasiswa/" + nim + "/transkrip")
                .header("X-App-Key", environment.getProperty("env.data.app-key"))
                .header("X-Secret-Key", environment.getProperty("env.data.secret-key"))
                .retrieve()
                .body(KhsSevimaResp.class);
        assert khsSevimaResp != null;
        List<KhsSevimaRespDataAttributes> khsSevimaRespDataAttributes = new ArrayList<>();
        khsSevimaResp.getData().forEach(n -> khsSevimaRespDataAttributes.add(n.getAttributes()));
        khsSevimaRespDataAttributes.sort(Comparator.comparing(KhsSevimaRespDataAttributes::getKode_mata_kuliah));
        var matkulDiakuiList = mataKuliahRepo.findByIsDisabledOrderById(false);
        var matkulMhs = new ArrayList<KhsSevimaRespDataAttributes>();
        int j = 0;
        int startJ = 0;
        for (MataKuliah mataKuliah : matkulDiakuiList) {
            while (j < khsSevimaRespDataAttributes.size()) {
                if (mataKuliah.getId().equals(khsSevimaRespDataAttributes.get(j).getKode_mata_kuliah())) {
                    matkulMhs.add(khsSevimaRespDataAttributes.get(j));
                    startJ = j;
                    break;
                }
                j++;
            }
            if (matkulMhs.isEmpty()) {
                j = 0;
            } else {
                j = startJ;
            }
        }
        return matkulMhs;
    }

    @Override
    @Transactional
    public void clearMataKuliahMahasiswa(String nim) {
        var mahasiswa = mahasiswaRepo.findById(nim).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa tidak ditemukan!"));
        mahasiswa.getMataKuliah().clear();
        mahasiswa.setIsLulusMatkul(false);
    }
}
