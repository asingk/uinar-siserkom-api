package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.dto.NilaiUjianUpdateReq;
import id.ac.arraniry.siserkomapi.entity.NilaiUjian;
import id.ac.arraniry.siserkomapi.service.NilaiUjianService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/nilai-ujian")
@CrossOrigin
public class NilaiUjianRest {
    private final NilaiUjianService nilaiUjianService;

    public NilaiUjianRest(NilaiUjianService nilaiUjianService) {
        this.nilaiUjianService = nilaiUjianService;
    }

    @Operation(summary = "Mendapatakan daftar nilai ujian")
    @GetMapping()
    public List<NilaiUjian> getAll() {
        return nilaiUjianService.findByOrderByNilaiAngkaDesc();
    }

    @Operation(summary = "Mendapatkan nilai ujian berdasarkan id")
    @GetMapping("/{id}")
    public NilaiUjian getById(@PathVariable String id) {
        var result = nilaiUjianService.findById(id);
        if (result.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nilai ujian tidak ditemukan");
        return result.get();
    }

    @Operation(summary = "Mengubah nilai ujian")
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody NilaiUjianUpdateReq req) {
        nilaiUjianService.update(id, req);
    }
}
