package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Invoice {
    @Id
    private String id;
    private Boolean isSudahBayar;
    private Boolean isExpired;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sevimaInvKode;
    private String sevimaInvId;
    private String nilaiUjianHuruf;
    private Integer nilaiUjianAngka;
    private Boolean isLulusUjian;

    @ManyToOne
    @JoinColumn(name="nim")
    private Mahasiswa mahasiswa;
    @ManyToOne
    @JoinColumn(name = "id_kelas")
    private Kelas kelas;
    @ManyToOne
    @JoinColumn(name = "id_jenis_invoice")
    private JenisInvoice jenisInvoice;
}
