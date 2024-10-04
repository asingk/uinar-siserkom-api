package id.ac.arraniry.siserkomapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatedByReq {
    @NotBlank
    private String updatedBy;
}
