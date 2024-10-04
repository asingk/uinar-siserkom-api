package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.NilaiUjianUpdateReq;
import id.ac.arraniry.siserkomapi.entity.NilaiUjian;
import id.ac.arraniry.siserkomapi.repository.NilaiUjianRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NilaiUjianServiceImpl implements NilaiUjianService {
    private final NilaiUjianRepo nilaiUjianRepo;

    public NilaiUjianServiceImpl(NilaiUjianRepo nilaiUjianRepo) {
        this.nilaiUjianRepo = nilaiUjianRepo;
    }

    @Override
    public List<NilaiUjian> findByOrderByNilaiAngkaDesc() {
        return nilaiUjianRepo.findByOrderByNilaiAngkaDesc();
    }

    @Override
    public Optional<NilaiUjian> findById(String id) {
        return nilaiUjianRepo.findById(id);
    }

    @Override
    public void update(String id, NilaiUjianUpdateReq req) {
        var nilaiUjian = new NilaiUjian();
        nilaiUjian.setId(id);
        nilaiUjian.setNilaiAngka(req.getNilaiAngka());
        nilaiUjian.setIsLulus(req.getIsLulus());
        nilaiUjian.setUpdatedBy(req.getUpdatedBy());
        nilaiUjian.setUpdatedAt(LocalDateTime.now());
        nilaiUjianRepo.save(nilaiUjian);
    }
}
