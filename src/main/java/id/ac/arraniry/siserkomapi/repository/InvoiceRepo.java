package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<Invoice, String> {
    List<Invoice> findByMahasiswaIdOrderByCreatedAtDesc(String nim);
    Optional<Invoice> findByIdAndKelasId(String noInvoice, Integer kelasId);
    Page<Invoice> findByIdStartsWithOrMahasiswaIdStartsWith(String id, String nim, Pageable pageable);
    List<Invoice> findByKelasIdOrderByMahasiswaNamaAsc(Integer kelasId);
    Optional<Invoice> findByMahasiswaIdAndIsLulusUjian(String nim, Boolean isLulusUjian);
    Optional<Invoice> findByMahasiswaIdAndJenisInvoiceIdAndIsSudahBayar(String nim, Integer jenisInvoiceId, Boolean isSudahBayar);
}
