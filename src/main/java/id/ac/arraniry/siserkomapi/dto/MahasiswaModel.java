package id.ac.arraniry.siserkomapi.dto;

import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MahasiswaModel extends RepresentationModel<MahasiswaModel> {
    private String nim;
    private String nama;
    private String prodi;
    private String certificateNo;
    private String certificateUrl;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Boolean isLulusMataKuliah;
//    private List<MataKuliahMahasiswaDto> mataKuliah = new ArrayList<MataKuliahMahasiswaDto>();

    public MahasiswaModel(Mahasiswa mahasiswa) {
        this.nim = mahasiswa.getId();
        this.nama = mahasiswa.getNama();
        this.prodi = mahasiswa.getProdi();
        this.certificateNo = mahasiswa.getCertificateNo();
        if(null != mahasiswa.getCertificateNo())
            this.certificateUrl = GlobalConstants.SERTIFIKAT_URL + "/" + mahasiswa.getCertificateNo();
        this.updatedAt = mahasiswa.getUpdatedAt();
        this.createdAt = mahasiswa.getCreatedAt();
        this.isLulusMataKuliah = mahasiswa.getIsLulusMatkul();
//        if(mahasiswa.getMataKuliah() != null && !mahasiswa.getMataKuliah().isEmpty()) {
//            mahasiswa.getMataKuliah().forEach(row -> {
//                var dto = new MataKuliahMahasiswaDto();
//                dto.setId(row.getMataKuliah().getId());
//                dto.setNama(row.getMataKuliah().getNama());
//                dto.setNilai(row.getNilai());
//                dto.setIsLulus(row.getIsLulus());
//                dto.setCreatedAt(row.getCreatedAt());
//                this.mataKuliah.add(dto);
//            });
//        }
    }
}
