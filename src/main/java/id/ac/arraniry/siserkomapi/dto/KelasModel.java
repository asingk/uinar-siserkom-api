package id.ac.arraniry.siserkomapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class KelasModel extends RepresentationModel<KelasModel> {
    private Integer id;
    private Integer idPengajar;
    private String namaPengajar;
    private Integer idJenis;
    private String namaJenis;
    private LocalDate tanggal;
    private LocalTime jam;
    private Integer isi;
    private Boolean isPenuh;
    @JsonIgnore
    private Integer version;
    private Integer dayaTampung;
    private Integer tersedia;

    public KelasModel(Kelas kelas) {
        this.id = kelas.getId();
        this.idPengajar = kelas.getPengajar().getId();
        this.namaPengajar = kelas.getPengajar().getNama();
        this.idJenis = kelas.getJenisInvoice().getId();
        this.namaJenis = kelas.getJenisInvoice().getNama();
        this.tanggal = kelas.getTanggal();
        this.jam = kelas.getJam();
        this.isi = kelas.getIsi();
        this.isPenuh = kelas.getIsPenuh();
        this.version = kelas.getVersion();
        this.dayaTampung = kelas.getDayaTampung();
        this.tersedia = this.dayaTampung - this.isi;
    }
}
