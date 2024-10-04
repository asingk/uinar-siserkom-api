package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.JenisInvoiceUpdateReq;
import id.ac.arraniry.siserkomapi.entity.JenisInvoice;
import id.ac.arraniry.siserkomapi.repository.JenisInvoiceRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JenisInvoiceServiceImpl implements JenisInvoiceService {
    private final JenisInvoiceRepo jenisInvRepo;

    public JenisInvoiceServiceImpl(JenisInvoiceRepo jenisInvRepo) {
        this.jenisInvRepo = jenisInvRepo;
    }

    @Override
    public List<JenisInvoice> findAll() {
        return jenisInvRepo.findAll();
    }

    @Override
    public Optional<JenisInvoice> findById(Integer id) {
        return jenisInvRepo.findById(id);
    }

    @Override
    public void update(Integer id, JenisInvoiceUpdateReq req) {
        var jenisInvoice = jenisInvRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis invoice tidak ditemukan!"));
        jenisInvoice.setNama(req.getNama());
        jenisInvoice.setBiaya(req.getBiaya());
        jenisInvoice.setUpdatedBy(req.getUpdatedBy());
        jenisInvoice.setUpdatedAt(LocalDateTime.now());
        jenisInvRepo.save(jenisInvoice);
    }
}
