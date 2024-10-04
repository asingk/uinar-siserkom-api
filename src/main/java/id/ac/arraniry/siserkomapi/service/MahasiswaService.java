package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.MahasiswaModel;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MahasiswaService {
    Optional<Mahasiswa> findMahasiswaById(String id);
//    void save(Mahasiswa mahasiswa);
//    void clearMataKuliahMahasiswa(String nim);
    Page<Mahasiswa> findByIdOrNama(String searchString, Pageable pageable);
//    MahasiswaDto updateMataKuliah(String nim);
//    Mahasiswa syncMahasiswa(String nim);
}
