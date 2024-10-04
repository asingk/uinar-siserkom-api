package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JenisInvoiceUpdateReq {
    @NotBlank
    private String nama;
    @NotNull
    @Min(0)
    private Integer biaya;
    @NotBlank
    private String updatedBy;
}
