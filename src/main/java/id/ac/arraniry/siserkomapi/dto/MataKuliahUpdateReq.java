package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MataKuliahUpdateReq {
    @NotBlank
    @Size(min = 2, max = 100)
    private String nama;
    @NotNull
    private Boolean isDisabled;
    @NotBlank
    private String updatedBy;
}
