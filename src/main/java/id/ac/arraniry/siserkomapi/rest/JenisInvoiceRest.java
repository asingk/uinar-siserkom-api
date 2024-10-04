package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.dto.JenisInvoiceUpdateReq;
import id.ac.arraniry.siserkomapi.entity.JenisInvoice;
import id.ac.arraniry.siserkomapi.service.JenisInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/jenis-invoice")
public class JenisInvoiceRest {
    private final JenisInvoiceService jenisInvoiceService;

    public JenisInvoiceRest(JenisInvoiceService invService) {
        this.jenisInvoiceService = invService;
    }

    @Operation(summary = "Mendapatkan daftar jenis pembayaran")
    @GetMapping()
    public List<JenisInvoice> getJenisBayar() {
        return jenisInvoiceService.findAll();
    }

    @Operation(summary = "Mendapatkan jenis invoice berdasarkan id")
    @GetMapping("/{id}")
    public JenisInvoice getOneJenisBayar(@PathVariable Integer id) {
        return jenisInvoiceService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jenis invoice tidak ditemukan!"));
    }

    @Operation(summary = "Mengubah jenis invoice")
    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @Valid @RequestBody JenisInvoiceUpdateReq req) {
        jenisInvoiceService.update(id, req);
    }
}
