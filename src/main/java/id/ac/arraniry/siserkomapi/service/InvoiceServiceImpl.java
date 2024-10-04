package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.assembler.InvoiceModelAssembler;
import id.ac.arraniry.siserkomapi.dto.UpdatedByReq;
import id.ac.arraniry.siserkomapi.dto.NilaiUjianMahasiswaReq;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import id.ac.arraniry.siserkomapi.entity.NilaiUjian;
import id.ac.arraniry.siserkomapi.repository.InvoiceRepo;
import id.ac.arraniry.siserkomapi.repository.KelasRepo;
import id.ac.arraniry.siserkomapi.repository.NilaiUjianRepo;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final KelasRepo kelasRepo;
    private final NilaiUjianRepo nilaiUjianRepo;
    private final InvoiceModelAssembler invoiceModelAssembler;

    public InvoiceServiceImpl(InvoiceRepo invoiceRepo, KelasRepo kelasRepo, NilaiUjianRepo nilaiUjianRepo, InvoiceModelAssembler invoiceModelAssembler) {
        this.invoiceRepo = invoiceRepo;
        this.kelasRepo = kelasRepo;
        this.nilaiUjianRepo = nilaiUjianRepo;
        this.invoiceModelAssembler = invoiceModelAssembler;
    }

    @Override
    public Page<Invoice> findByIdStartsWithOrMahasiswaIdStartsWith(String id, String nim, Pageable pageable) {
        return invoiceRepo.findByIdStartsWithOrMahasiswaIdStartsWith(id, nim, pageable);
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return invoiceRepo.findById(id);
    }

    @Override
    public void deleteById(String id) {
        invoiceRepo.deleteById(id);
    }

    @Override
    public List<Invoice> findByNim(String nim) {
        return invoiceRepo.findByMahasiswaIdOrderByCreatedAtDesc(nim);
    }

    @Override
    public void selectKelas(String noInvoice, Integer idKelas, UpdatedByReq req) {
        var kelas = kelasRepo.findById(idKelas).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kelas tidak ditemukan!"));
        if(kelas.getIsPenuh()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Kelas tidak tersedia!");
        }
        var invoice = invoiceRepo.findById(noInvoice).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        if(invoice.getIsExpired()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice sudah pernah digunakan!");
        if(!invoice.getJenisInvoice().equals(kelas.getJenisInvoice()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Jenis sertifikat invoice berbeda!");
        invoice.setKelas(kelas);
        invoice.setIsExpired(true);
        invoice.setUpdatedBy(req.getUpdatedBy());
        invoice.setUpdatedAt(LocalDateTime.now());
        kelas.setIsi(kelas.getIsi() + 1);
        kelas.setVersion(kelas.getVersion() + 1);
        if(kelas.getIsi() >= kelas.getDayaTampung()) kelas.setIsPenuh(true);
        invoiceRepo.save(invoice);
    }

    @Override
    public void updateNilaiUjian(String id, NilaiUjianMahasiswaReq req) {
        var invoice = invoiceRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        if(Objects.equals(invoice.getJenisInvoice().getId(), GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invoice buat sertifikat tidak ada nilai ujian!!");
        if(!invoice.getIsSudahBayar()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice ini belum dibayar!!");
        if(!invoice.getIsExpired()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invoice belum digunakan!!");
        List<NilaiUjian> nilaiUjianList = nilaiUjianRepo.findByOrderByNilaiAngkaDesc();
        invoice.setNilaiUjianAngka(req.getNilai());
        for (NilaiUjian row: nilaiUjianList) {
            if (req.getNilai() >= row.getNilaiAngka()) {
                invoice.setNilaiUjianHuruf(row.getId());
                invoice.setIsLulusUjian(row.getIsLulus());
                break;
            }
        }
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setUpdatedBy(req.getUpdatedBy());
        invoiceRepo.save(invoice);
    }
}
