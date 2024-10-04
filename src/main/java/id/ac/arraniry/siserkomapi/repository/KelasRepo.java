package id.ac.arraniry.siserkomapi.repository;

import id.ac.arraniry.siserkomapi.entity.JenisInvoice;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import id.ac.arraniry.siserkomapi.entity.Pengajar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KelasRepo extends JpaRepository<Kelas, Integer> {
    Page<Kelas> findByJenisInvoiceInAndIsPenuhIn(Pageable paging, List<JenisInvoice> jenisSertifikat, List<Boolean> isPenuh);
    Page<Kelas> findByJenisInvoiceInAndIsPenuhInAndPengajar(Pageable paging, List<JenisInvoice> jenisInvoice, List<Boolean> isPenuh, Pengajar pengajar);
    List<Kelas> findByJenisInvoiceIdAndIsPenuh(Integer jenisInvoiceId, Boolean isPenuh);
}
