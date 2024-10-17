package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.entity.MataKuliahMahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MahasiswaService {
    Optional<Mahasiswa> findById(String id);
    Mahasiswa save(Mahasiswa mahasiswa);
    void clearMataKuliahMahasiswa(String nim);
    Page<Mahasiswa> findByIdOrNama(String searchString, Pageable pageable);
    Mahasiswa sync(String nim);
    List<MataKuliahMahasiswa> syncMataKuliah(String nim);
}
