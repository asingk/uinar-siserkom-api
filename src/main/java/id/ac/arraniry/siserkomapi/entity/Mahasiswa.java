package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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
    @OneToMany(mappedBy = "mahasiswa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MataKuliahMahasiswa> mataKuliah;
    @OneToMany(mappedBy = "mahasiswa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invoice> invoice;
}
