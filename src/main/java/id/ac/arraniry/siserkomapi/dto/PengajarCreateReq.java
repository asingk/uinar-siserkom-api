package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PengajarCreateReq {
    @NotBlank
    private String username;
    @NotBlank
    private String nama;
    @NotBlank
    private String createdBy;
}
