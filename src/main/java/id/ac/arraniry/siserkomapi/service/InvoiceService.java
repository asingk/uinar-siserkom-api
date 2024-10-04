package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.UpdatedByReq;
import id.ac.arraniry.siserkomapi.dto.NilaiUjianMahasiswaReq;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Page<Invoice> findByIdStartsWithOrMahasiswaIdStartsWith(String id, String nim, Pageable pageable);;
    Optional<Invoice> findById(String id);
    void deleteById(String id);
//    Invoice findLatestInvoice(String nim);
    List<Invoice> findByNim(String nim);
//    Invoice create(String nim, Integer jenisInvoice);
    void selectKelas(String noInvoice, Integer idKelas, UpdatedByReq req);
    void updateNilaiUjian(String id, NilaiUjianMahasiswaReq req);
//    void updateBayar(String id, String sevimaInv);
//    List<Invoice> findByMahasiswaIdAndIsLulusUjianOrderByCreatedAtDesc(String nim, Boolean isLulusUjian);
}
