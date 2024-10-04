package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NilaiUjianMahasiswaReq {
    @NotNull
    @Min(0)
    @Max(100)
    private Integer nilai;
    @NotBlank
    private String updatedBy;
}
