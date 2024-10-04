package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Mahasiswa {
    @Id
    private String id;
    private String nama;
    private String prodi;
    private String certificateNo;
    @Column(name = "is_lulus_mk")
    private Boolean isLulusMatkul;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
