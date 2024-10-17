package id.ac.arraniry.siserkomapi.dto;

import id.ac.arraniry.siserkomapi.entity.MataKuliahMahasiswa;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MataKuliahMahasiswaModel {
    private Integer id;
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String nilai;
    private Boolean isLulus;
    private LocalDateTime createdAt;

    public MataKuliahMahasiswaModel(MataKuliahMahasiswa entity) {
        this.id = entity.getId();
        this.kodeMataKuliah = entity.getMataKuliah().getId();
        this.namaMataKuliah = entity.getMataKuliah().getNama();
        this.nilai = entity.getNilai();
        this.isLulus = entity.getIsLulus();
        this.createdAt = entity.getCreatedAt();
    }
}
