package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.KelasCreateReq;
import id.ac.arraniry.siserkomapi.dto.KelasUpdateReq;
import id.ac.arraniry.siserkomapi.dto.UpdatedByReq;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import id.ac.arraniry.siserkomapi.repository.InvoiceRepo;
import id.ac.arraniry.siserkomapi.repository.JenisInvoiceRepo;
import id.ac.arraniry.siserkomapi.repository.KelasRepo;
import id.ac.arraniry.siserkomapi.repository.PengajarRepo;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KelasServiceImpl implements KelasService {
    private final KelasRepo kelasRepo;
    private final PengajarRepo pengajarRepo;
    private final JenisInvoiceRepo jenisInvoiceRepo;
    private final InvoiceRepo invoiceRepo;

    public KelasServiceImpl(KelasRepo kelasRepo, PengajarRepo pengajarRepo, JenisInvoiceRepo jenisInvoiceRepo, InvoiceRepo invoiceRepo) {
        this.kelasRepo = kelasRepo;
        this.pengajarRepo = pengajarRepo;
        this.jenisInvoiceRepo = jenisInvoiceRepo;
        this.invoiceRepo = invoiceRepo;
    }

    @Override
    public Page<Kelas> findAll(Pageable paging) {
        return kelasRepo.findAll(paging);
    }

    @Override
    public Optional<Kelas> findById(Integer id) {
        return kelasRepo.findById(id);
    }

    @Override
    public List<Invoice> findInvoiceByKelas(Integer id) {
        return invoiceRepo.findByKelasIdOrderByMahasiswaNamaAsc(id);
    }

    @Override
    public Kelas create(KelasCreateReq req) {
        if(GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT.equals(req.getIdJenisInvoice())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "tidak ada kelas untuk buat sertifikat!");
        }
        if(req.getWaktu().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "waktu telah berlalu!");
        }
        var pengajar = pengajarRepo.findByIdAndIsDisabled(req.getIdPengajar(), false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengajar tidak ditemukan!"));
        var jenisInvoice = jenisInvoiceRepo.findById(req.getIdJenisInvoice())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Invoice tidak ditemukan!"));
        var kelas = new Kelas();
        kelas.setJenisInvoice(jenisInvoice);
        kelas.setTanggal(req.getWaktu().toLocalDate());
        kelas.setJam(req.getWaktu().toLocalTime());
        kelas.setDayaTampung(req.getDayaTampung());
        kelas.setPengajar(pengajar);
        kelas.setIsi(0);
        kelas.setIsPenuh(false);
        kelas.setVersion(0);
        kelas.setCreatedBy(req.getCreatedBy());
        kelas.setCreatedAt(LocalDateTime.now());
        return kelasRepo.save(kelas);
    }

    @Override
    public void deleteInvoiceFromKelas(Integer id, String noInvoice, UpdatedByReq req) {
        var inv = invoiceRepo.findByIdAndKelasId(noInvoice, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak ditemukan!"));
        inv.getKelas().setIsi(inv.getKelas().getIsi()-1);
        if(inv.getKelas().getIsPenuh()) inv.getKelas().setIsPenuh(false);
        inv.getKelas().setVersion(inv.getKelas().getVersion()+1);
        inv.setKelas(null);
        inv.setIsExpired(false);
        inv.setUpdatedBy(req.getUpdatedBy());
        inv.setUpdatedAt(LocalDateTime.now());
        invoiceRepo.save(inv);
    }

    @Override
    public void updateById(Integer id, KelasUpdateReq req) {
        var kelas = findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kelas tidak ditemukan!"));
        if(GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT.equals(req.getIdJenisInvoice())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "tidak ada kelas untuk buat sertifikat!");
        }
        if(req.getWaktu().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "waktu telah berlalu!");
        }
        var pengajar = pengajarRepo.findByIdAndIsDisabled(req.getIdPengajar(), false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengajar tidak ditemukan!"));
        var jenisInvoice = jenisInvoiceRepo.findById(req.getIdJenisInvoice())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis Invoice tidak ditemukan!"));
        kelas.setDayaTampung(req.getDayaTampung());
        kelas.setTanggal(req.getWaktu().toLocalDate());
        kelas.setJam(req.getWaktu().toLocalTime());
        kelas.setPengajar(pengajar);
        kelas.setJenisInvoice(jenisInvoice);
        kelas.setVersion(kelas.getVersion() + 1);
        kelas.setIsPenuh(req.getDayaTampung() <= kelas.getIsi());
        kelas.setUpdatedBy(req.getUpdatedBy());
        kelas.setUpdatedAt(LocalDateTime.now());
        kelasRepo.save(kelas);
    }

    @Override
    public List<Kelas> findAvailable(Integer idJenisInvoice) {
        var result = kelasRepo.findByJenisInvoiceIdAndIsPenuh(idJenisInvoice, false);
        result.removeIf(n -> LocalDateTime.of(n.getTanggal(), n.getJam()).isBefore(LocalDateTime.now()));
        return result;
    }
}
