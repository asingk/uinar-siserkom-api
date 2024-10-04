package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MahasiswaRepo extends JpaRepository<Mahasiswa, String> {
    Page<Mahasiswa> findByIdStartsWithOrNamaContainingIgnoreCase(String id, String nama, Pageable pageable);
}
