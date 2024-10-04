package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate tanggal;
    private LocalTime jam;
    private Integer isi = 0;
    @Column(name = "ver")
    private Integer version = 0;
    private Boolean isPenuh = false;
    private Integer dayaTampung;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    @OneToMany(mappedBy = "kelas")
//    private Set<Invoice> invoice = new HashSet<>();
    @ManyToOne
    @JoinColumn(name="id_pengajar")
    private Pengajar pengajar;
    @ManyToOne
    @JoinColumn(name = "id_jenis_invoice")
    private JenisInvoice jenisInvoice;
}
