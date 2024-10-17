package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MataKuliahRepo extends JpaRepository<MataKuliah, String> {
    List<MataKuliah> findByIsDisabledOrderById(Boolean isDisabled);
    Optional<MataKuliah> findByIdAndIsDisabled(String id, Boolean isDisabled);
    Page<MataKuliah> findByIdStartsWithOrNamaContainingIgnoreCase(String id, String nama, Pageable pageable);
    @Query("select mk from MataKuliah mk where (mk.id like ?1% or mk.nama like %?2%) and mk.isDisabled = ?3")
    Page<MataKuliah> findByIdStartsWithOrNamaContainingIgnoreCaseAndIsDisabled(String id, String nama, Boolean isDisabled, Pageable pageable);
}
