package id.ac.arraniry.siserkomapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class JenisInvoice {
    @Id
    private Integer id;
    private String nama;
    private Integer biaya;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
