package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.dto.PengajarCreateReq;
import id.ac.arraniry.siserkomapi.dto.PengajarUpdateReq;
import id.ac.arraniry.siserkomapi.entity.Pengajar;
import id.ac.arraniry.siserkomapi.service.PengajarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/pengajar")
@CrossOrigin
public class PengajarRest {
    private final PengajarService pengajarService;

    public PengajarRest(PengajarService pengajarService) {
        this.pengajarService = pengajarService;
    }

    @Operation(summary = "Mendapatkan detail pengajar berdasarkan id")
    @GetMapping("/{id}")
    public Pengajar getById(@PathVariable Integer id) {
        return pengajarService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengajar id " + id + " tidak ditemukan"));
    }

    @Operation(summary = "Mendapatakan daftar pengajar")
    @GetMapping()
    public List<Pengajar> getAll() {
        return pengajarService.findAll();
    }

    @Operation(summary = "Membuat pengajar baru")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Pengajar create(@Valid @RequestBody PengajarCreateReq req) {
        return pengajarService.create(req);
    }

    @Operation(summary = "Mengubah pengajar")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable Integer id, @Valid @RequestBody PengajarUpdateReq req) {
        pengajarService.update(id, req);
    }

    @Operation(summary = "Menghapus pengajar")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @PathVariable Integer id) {
        pengajarService.deleteById(id);
    }
}
