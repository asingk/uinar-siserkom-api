package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MataKuliahCreateReq {
    @NotBlank
    @Size(min = 2, max = 15)
    private String id;
    @NotBlank
    @Size(min = 2, max = 100)
    private String nama;
    @NotBlank
    private String createdBy;
}
