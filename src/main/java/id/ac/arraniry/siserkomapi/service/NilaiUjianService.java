package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.NilaiUjianUpdateReq;
import id.ac.arraniry.siserkomapi.entity.NilaiUjian;

import java.util.List;
import java.util.Optional;

public interface NilaiUjianService {
    List<NilaiUjian> findByOrderByNilaiAngkaDesc();
    Optional<NilaiUjian> findById(String id);
    void update(String id, NilaiUjianUpdateReq req);
}
