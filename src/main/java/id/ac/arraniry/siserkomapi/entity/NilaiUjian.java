package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class NilaiUjian {
    @Id
    private String id;
    private Integer nilaiAngka;
    private Boolean isLulus;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
