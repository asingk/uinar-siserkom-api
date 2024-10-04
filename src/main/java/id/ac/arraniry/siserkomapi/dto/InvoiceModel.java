package id.ac.arraniry.siserkomapi.dto;

import id.ac.arraniry.siserkomapi.entity.Invoice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceModel extends RepresentationModel<InvoiceModel> {
    private String id;
    private Boolean isSudahBayar;
    private Boolean isExpired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sevimaInv;
    private String nim;
    private String namaMahasiswa;
    private Integer idKelas;
    private Integer idJenis;
    private String namaJenis;
    private String nilaiUjianHuruf;
    private Integer nilaiUjianAngka;
    private Boolean isLulusUjian;

    public InvoiceModel(Invoice entity) {
        this.id = entity.getId();
        this.isSudahBayar = entity.getIsSudahBayar();
        this.isExpired = entity.getIsExpired();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.sevimaInv = entity.getSevimaInvKode();
        this.nim = entity.getMahasiswa().getId();
        this.namaMahasiswa = entity.getMahasiswa().getNama();
        if (null != entity.getKelas()) this.idKelas = entity.getKelas().getId();
        this.idJenis = entity.getJenisInvoice().getId();
        this.namaJenis = entity.getJenisInvoice().getNama();
        this.nilaiUjianHuruf = entity.getNilaiUjianHuruf();
        this.nilaiUjianAngka = entity.getNilaiUjianAngka();
        this.isLulusUjian = entity.getIsLulusUjian();
    }
}
