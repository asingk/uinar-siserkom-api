package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.MataKuliahCreateReq;
import id.ac.arraniry.siserkomapi.dto.MataKuliahUpdateReq;
import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MataKuliahService {
    Optional<MataKuliah> findById(String id);
    Page<MataKuliah> findAllCollectionModel(String id, String nama, Boolean disabled, Pageable pageable);
    MataKuliah create(MataKuliahCreateReq req);
    void update(String id, MataKuliahUpdateReq req);
    void delete(String id);
}
