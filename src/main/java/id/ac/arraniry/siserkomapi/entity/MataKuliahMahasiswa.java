package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "mk_mahasiswa")
public class MataKuliahMahasiswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nilai;
    private Boolean isLulus = false;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "nim")
    private Mahasiswa mahasiswa;
    @ManyToOne
    @JoinColumn(name = "kode_mk_sevima")
    private MataKuliah mataKuliah;
}
