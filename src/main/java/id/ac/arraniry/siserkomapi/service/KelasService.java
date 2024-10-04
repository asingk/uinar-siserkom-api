package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.KelasCreateReq;
import id.ac.arraniry.siserkomapi.dto.KelasUpdateReq;
import id.ac.arraniry.siserkomapi.dto.UpdatedByReq;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KelasService {
    Page<Kelas> findAll(Pageable paging);
    Optional<Kelas> findById(Integer id);
    List<Invoice> findInvoiceByKelas(Integer id);
    Kelas create(KelasCreateReq req);
    void deleteInvoiceFromKelas(Integer id, String noInvoice, UpdatedByReq req);
//    Kelas findKelasByNimAndNoInvoice(String nim, String noInvoice);
    void updateById(Integer id, KelasUpdateReq req);
    List<Kelas> findAvailable(Integer idJenisInvoice);
}
