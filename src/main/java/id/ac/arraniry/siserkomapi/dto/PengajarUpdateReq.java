package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PengajarUpdateReq {
    @NotBlank
    private String username;
    @NotBlank
    private String nama;
    @NotNull
    private Boolean isDisabled;
    @NotBlank
    private String updatedBy;
}
