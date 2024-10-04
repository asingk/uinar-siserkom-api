package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.MahasiswaModel;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.repository.MahasiswaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MahasiswaServiceImpl implements MahasiswaService {
    private final MahasiswaRepo mahasiswaRepo;

    public MahasiswaServiceImpl(MahasiswaRepo mahasiswaRepo) {
        this.mahasiswaRepo = mahasiswaRepo;
    }

    //    private final MataKuliahRepository matkulRepo;
//    private final Environment environment;
//    private final MataKuliahRepository mataKuliahRepository;
    @Override
    public Optional<Mahasiswa> findMahasiswaById(String id) {
        return mahasiswaRepo.findById(id);
    }

    @Override
    public Page<Mahasiswa> findByIdOrNama(String searchString, Pageable pageable) {
        return mahasiswaRepo.findByIdStartsWithOrNamaContainingIgnoreCase(searchString, searchString, pageable);
    }
}
