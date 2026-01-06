package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.*;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import id.ac.arraniry.siserkomapi.entity.NilaiUjian;
import id.ac.arraniry.siserkomapi.repository.*;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import org.jspecify.annotations.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final KelasRepo kelasRepo;
    private final NilaiUjianRepo nilaiUjianRepo;
    private final MahasiswaRepo mahasiswaRepo;
    private final JenisInvoiceRepo jenisInvoiceRepo;
    private final Environment environment;

    RestClient restClient = RestClient.create();

    public InvoiceServiceImpl(InvoiceRepo invoiceRepo, KelasRepo kelasRepo, NilaiUjianRepo nilaiUjianRepo, MahasiswaRepo mahasiswaRepo,
                              JenisInvoiceRepo jenisInvoiceRepo, Environment environment) {
        this.invoiceRepo = invoiceRepo;
        this.kelasRepo = kelasRepo;
        this.nilaiUjianRepo = nilaiUjianRepo;
        this.mahasiswaRepo = mahasiswaRepo;
        this.jenisInvoiceRepo = jenisInvoiceRepo;
        this.environment = environment;
    }

    @Override
    public Page<Invoice> findByIdStartsWithOrMahasiswaIdStartsWith(String id, String nim, Pageable pageable) {
        return invoiceRepo.findByIdStartsWithOrMahasiswaIdStartsWith(id, nim, pageable);
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return invoiceRepo.findById(id);
    }

    @Override
    public void deleteById(String id) {
        invoiceRepo.deleteById(id);
    }

    @Override
    public List<Invoice> findByNim(String nim) {
        return invoiceRepo.findByMahasiswaIdOrderByCreatedAtDesc(nim);
    }

    @Override
    @Transactional
    public Invoice create(String nim, Integer jenisInvoice) {
        var mahasiswa = mahasiswaRepo.findById(nim).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa tidak ditemukan!"));
        if(null != mahasiswa.getCertificateNo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda sudah mencetak sertifikat!");
        }
        if(mahasiswa.getIsLulusMatkul() && !GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT.equals(jenisInvoice)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda sudah lulus mata kuliah komputer!");
        } else if (!mahasiswa.getIsLulusMatkul() && GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT.equals(jenisInvoice)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak lulus mata kuliah komputer!");
        }
        mahasiswa.getInvoice().forEach(row -> {
            if(!row.getIsExpired() && jenisInvoice.equals(row.getJenisInvoice().getId()))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice sebelumnya belum digunakan!");
            if(row.getJenisInvoice().getId().equals(GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT) && row.getIsSudahBayar())
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice buat sertifikat sudah dibayar, silahkan cetak sertifikat!");
        });
        var invoice = new Invoice();
        invoice.setId(generateNoInvoice(jenisInvoice));
        invoice.setJenisInvoice(jenisInvoiceRepo.findById(jenisInvoice)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis invoice tidak ditemukan!")));
        invoice.setIsSudahBayar(false);
        invoice.setIsExpired(false);
        invoice.setMahasiswa(mahasiswa);
        invoice.setCreatedAt(LocalDateTime.now());
        mahasiswa.getInvoice().add(invoice);
        return invoice;
    }

    private String generateNoInvoice(Integer kodeBayar) {
        LocalDate now = LocalDate.now();
        int tahun;
        int semester;
        if (now.getMonthValue() > 1 && now.getMonthValue() < 8) {
            semester = 2;
            tahun = now.getYear();
        } else if (now.getMonthValue() > 7) {
            semester = 1;
            tahun = now.getYear();
        } else {
            semester = 1;
            tahun = now.getYear()-1;
        }
        return kodeBayar.toString() + Integer.toString(tahun).substring(2, 4) + semester + randomString();
    }

    private String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 57; // numeral '9'
        int targetStringLength = 5;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public void selectKelas(String noInvoice, Integer idKelas, UpdatedByReq req) {
        var kelas = kelasRepo.findById(idKelas).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kelas tidak ditemukan!"));
        if(kelas.getIsPenuh()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Kelas tidak tersedia!");
        }
        var invoice = invoiceRepo.findById(noInvoice).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        if(invoice.getIsExpired()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice sudah pernah digunakan!");
        if(!invoice.getJenisInvoice().equals(kelas.getJenisInvoice()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Jenis sertifikat invoice berbeda!");
        invoice.setKelas(kelas);
        invoice.setIsExpired(true);
        invoice.setUpdatedBy(req.getUpdatedBy());
        invoice.setUpdatedAt(LocalDateTime.now());
        kelas.setIsi(kelas.getIsi() + 1);
        kelas.setVersion(kelas.getVersion() + 1);
        if(kelas.getIsi() >= kelas.getDayaTampung()) kelas.setIsPenuh(true);
        invoiceRepo.save(invoice);
    }

    @Override
    public void updateNilaiUjian(String id, NilaiUjianMahasiswaReq req) {
        var invoice = invoiceRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        if(Objects.equals(invoice.getJenisInvoice().getId(), GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invoice buat sertifikat tidak ada nilai ujian!!");
        if(!invoice.getIsSudahBayar()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice ini belum dibayar!!");
        if(!invoice.getIsExpired()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice belum digunakan!!");
        List<NilaiUjian> nilaiUjianList = nilaiUjianRepo.findByOrderByNilaiAngkaDesc();
        invoice.setNilaiUjianAngka(req.getNilai());
        for (NilaiUjian row: nilaiUjianList) {
            if (req.getNilai() >= row.getNilaiAngka()) {
                invoice.setNilaiUjianHuruf(row.getId());
                invoice.setIsLulusUjian(row.getIsLulus());
                break;
            }
        }
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setUpdatedBy(req.getUpdatedBy());
        invoiceRepo.save(invoice);
    }

    @Override
    public void updateBayar(String id) {
        var invoice = invoiceRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        if (invoice.getIsSudahBayar()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice ini sudah dibayar!!");
        var respDataList = getSevimaInvId(invoice);
        if (respDataList.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice belum dibayar!!");
        var respData = respDataList.get(0);
        var opt = invoiceRepo.findBySevimaInvId(respData.getId());
        if (opt.isPresent())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice belum dibayar!!");
        invoice.setSevimaInvId(respData.getId());
        invoice.setSevimaInvKode(respData.getAttributes().getKode_transaksi());
        invoice.setIsSudahBayar(true);
        if (GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT.equals(invoice.getJenisInvoice().getId())) invoice.setIsExpired(true);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepo.save(invoice);
    }

    @Override
    public Optional<Invoice> findByMahasiswaIdAndIsLulusUjian(String nim, Boolean isLulusUjian) {
        return invoiceRepo.findByMahasiswaIdAndIsLulusUjian(nim, isLulusUjian);
    }

    @Override
    public Optional<Invoice> findByMahasiswaIdAndJenisInvoiceIdAndIsSudahBayar(String nim, Integer jenisInvoiceId, Boolean isSudahBayar) {
        return invoiceRepo.findByMahasiswaIdAndJenisInvoiceIdAndIsSudahBayar(nim, jenisInvoiceId, isSudahBayar);
    }

    private List<InvoiceSevimaRespData> getSevimaInvId(Invoice invoice) {
        var invoiceSevimaResp = restClient.get()
                .uri(GlobalConstants.SIAKAD_API_URL + "/mahasiswa/" + invoice.getMahasiswa().getId() + "/invoice")
                .header("X-App-Key", environment.getProperty("env.data.app-key"))
                .header("X-Secret-Key", environment.getProperty("env.data.secret-key"))
                .retrieve()
                .body(InvoiceSevimaResp.class);
        assert invoiceSevimaResp != null;
        String sevimaJenisTagihan = getSevimaJenisTagihan(invoice);
        return invoiceSevimaResp.getData()
                .stream()
                .filter(row -> row.getAttributes().getIs_lunas().equals("1")
                                && row.getAttributes().getTanggal_transaksi().withZoneSameInstant(ZoneId.of("Asia/Jakarta")).toLocalDateTime()
                        .isAfter(invoice.getCreatedAt())
                                && row.getAttributes().getId_jenis_akun().equals(sevimaJenisTagihan))
                .sorted(Comparator.comparing(row -> row.getAttributes().getTanggal_transaksi(),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private static @NonNull String getSevimaJenisTagihan(Invoice invoice) {
        String sevimaJenisTagihan;
        if (invoice.getJenisInvoice().getId().equals(GlobalConstants.JENIS_INVOICE_PELATIHAN)) {
            sevimaJenisTagihan = GlobalConstants.SEVIMA_JENIS_INVOICE_PELATIHAN;
        } else if (invoice.getJenisInvoice().getId().equals(GlobalConstants.JENIS_INVOICE_UJIAN)) {
            sevimaJenisTagihan = GlobalConstants.SEVIMA_JENIS_INVOICE_UJIAN;
        } else if (invoice.getJenisInvoice().getId().equals(GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT)) {
            sevimaJenisTagihan = GlobalConstants.SEVIMA_JENIS_INVOICE_BUAT_SERTIFIKAT;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "jenis invoice tidak ditemukan!");
        }
        return sevimaJenisTagihan;
    }
}
