package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.NilaiUjian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NilaiUjianRepo extends JpaRepository<NilaiUjian, String> {
    List<NilaiUjian> findByOrderByNilaiAngkaDesc();
}
