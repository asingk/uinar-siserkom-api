package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.assembler.InvoiceModelAssembler;
import id.ac.arraniry.siserkomapi.dto.UpdatedByReq;
import id.ac.arraniry.siserkomapi.dto.InvoiceModel;
import id.ac.arraniry.siserkomapi.dto.NilaiUjianMahasiswaReq;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import id.ac.arraniry.siserkomapi.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/invoice")
public class InvoiceRest {
    private final InvoiceService invoiceService;
    private final PagedResourcesAssembler<Invoice> pagedResourcesAssembler;
    private final InvoiceModelAssembler invoiceModelAssembler;

    public InvoiceRest(InvoiceService invoiceService, PagedResourcesAssembler<Invoice> pagedResourcesAssembler, InvoiceModelAssembler invoiceModelAssembler) {
        this.invoiceService = invoiceService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.invoiceModelAssembler = invoiceModelAssembler;
    }

    @Operation(summary = "Mencari invoice berdasarkan nim atau nama")
    @GetMapping()
    public PagedModel<InvoiceModel> search(
            @RequestParam(defaultValue = "") String searchString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        if(size > 100) size = 100;
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var result = invoiceService.findByIdStartsWithOrMahasiswaIdStartsWith(searchString, searchString, pageable);
        return pagedResourcesAssembler.toModel(result, invoiceModelAssembler);
    }

    @Operation(summary = "Mendapatkan invoice berdasarkan id")
    @GetMapping("/{id}")
    public InvoiceModel getInvoiceById(@PathVariable String id) {
        var inv = invoiceService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice tidak"));
        return new InvoiceModel(inv);
    }

    @PostMapping("/{id}/nilai-ujian")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateNilaiUjian(@PathVariable String id, @Valid @RequestBody NilaiUjianMahasiswaReq req) {
        invoiceService.updateNilaiUjian(id, req);
    }

    @Operation(summary = "Membatalkan invoice")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable String id) {
        invoiceService.deleteById(id);
    }

    @PostMapping("/{id}/bayar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBayar(@PathVariable String id, @RequestParam String sevimaInv) {
        invoiceService.updateBayar(id, sevimaInv);
    }

    @Operation(summary = "Memilih kelas berdasarkan nomor invoice")
    @PostMapping("/{id}/kelas/{idKelas}")
    public void updateKelas(@PathVariable String id, @PathVariable Integer idKelas, @Valid @RequestBody UpdatedByReq req) {
        invoiceService.selectKelas(id, idKelas, req);
    }
}
