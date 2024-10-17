package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.MataKuliahCreateReq;
import id.ac.arraniry.siserkomapi.dto.MataKuliahUpdateReq;
import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import id.ac.arraniry.siserkomapi.repository.MataKuliahRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MataKuliahServiceImpl implements MataKuliahService {
    private final MataKuliahRepo matkulRepo;

    public MataKuliahServiceImpl(MataKuliahRepo matkulRepo) {
        this.matkulRepo = matkulRepo;
    }

    @Override
    public Optional<MataKuliah> findById(String id) {
        return matkulRepo.findById(id);
    }

    @Override
    public Page<MataKuliah> findAllCollectionModel(String id, String nama, Boolean disabled, Pageable pageable) {
        if (null == disabled) {
            return matkulRepo.findByIdStartsWithOrNamaContainingIgnoreCase(id, nama, pageable);
        } else {
            return matkulRepo.findByIdStartsWithOrNamaContainingIgnoreCaseAndIsDisabled(id, nama, disabled, pageable);
        }
    }

    @Override
    public MataKuliah create(MataKuliahCreateReq req) {
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setId(req.getId());
        mataKuliah.setNama(req.getNama());
        mataKuliah.setIsDisabled(false);
        mataKuliah.setCreatedAt(LocalDateTime.now());
        mataKuliah.setCreatedBy(req.getCreatedBy());
        return matkulRepo.save(mataKuliah);
    }

    @Override
    public void update(String id, MataKuliahUpdateReq req) {
        var matkul = matkulRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mata Kuliah tidak ditemukan!"));
        matkul.setNama(req.getNama());
        matkul.setIsDisabled(req.getIsDisabled());
        matkul.setUpdatedBy(req.getUpdatedBy());
        matkul.setUpdatedAt(LocalDateTime.now());
        matkulRepo.save(matkul);
    }

    @Override
    public void delete(String id) {
        matkulRepo.deleteById(id);
    }
}
