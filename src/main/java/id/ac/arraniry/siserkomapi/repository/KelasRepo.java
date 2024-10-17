package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KelasRepo extends JpaRepository<Kelas, Integer> {
    List<Kelas> findByJenisInvoiceIdAndIsPenuh(Integer jenisInvoiceId, Boolean isPenuh);
    List<Kelas> findByIsPenuh(Boolean isPenuh);
}
