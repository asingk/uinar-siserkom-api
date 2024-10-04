package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.PengajarCreateReq;
import id.ac.arraniry.siserkomapi.dto.PengajarUpdateReq;
import id.ac.arraniry.siserkomapi.entity.Pengajar;

import java.util.List;
import java.util.Optional;

public interface PengajarService {
    List<Pengajar> findAll();
    Pengajar create(PengajarCreateReq req);
    void update(Integer id, PengajarUpdateReq req);
    void deleteById(Integer id);
    Optional<Pengajar> findById(Integer id);
}
