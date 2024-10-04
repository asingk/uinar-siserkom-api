package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KelasCreateReq {
    @NotNull
    private Integer idPengajar;
    @NotNull
    private Integer idJenisInvoice;
    @NotNull
    private LocalDateTime waktu;
    @NotNull
    @Min(0)
    private Integer dayaTampung;
    @NotBlank
    private String createdBy;
}
