package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.Pengajar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PengajarRepo extends JpaRepository<Pengajar, Integer> {
    Optional<Pengajar> findByIdAndIsDisabled(Integer id, Boolean disabled);
    Optional<Pengajar> findByUsername(String username);
}
