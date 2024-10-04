package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.PengajarCreateReq;
import id.ac.arraniry.siserkomapi.dto.PengajarUpdateReq;
import id.ac.arraniry.siserkomapi.entity.Pengajar;
import id.ac.arraniry.siserkomapi.repository.PengajarRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PengajarServiceImpl implements PengajarService {
    private final PengajarRepo pengajarRepo;

    public PengajarServiceImpl(PengajarRepo pengajarRepo) {
        this.pengajarRepo = pengajarRepo;
    }

    @Override
    public List<Pengajar> findAll() {
        return pengajarRepo.findAll();
    }

    @Override
    public Pengajar create(PengajarCreateReq req) {
        Pengajar pengajar = new Pengajar();
        pengajar.setNama(req.getNama());
        pengajar.setUsername(req.getUsername());
        pengajar.setIsDisabled(false);
        pengajar.setCreatedBy(req.getCreatedBy());
        pengajar.setCreatedAt(LocalDateTime.now());
        return pengajarRepo.save(pengajar);
    }

    @Override
    public void update(Integer id, PengajarUpdateReq req) {
        var pengajar = pengajarRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengajar tidak ditemukan!"));
        pengajar.setNama(req.getNama());
        pengajar.setIsDisabled(req.getIsDisabled());
        pengajar.setUsername(req.getUsername());
        pengajar.setUpdatedBy(req.getUpdatedBy());
        pengajar.setUpdatedAt(LocalDateTime.now());
        pengajarRepo.save(pengajar);
    }

    @Override
    public void deleteById(Integer id) {
        pengajarRepo.deleteById(id);
    }

    @Override
    public Optional<Pengajar> findById(Integer id) {
        return pengajarRepo.findById(id);
    }
}
