package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.assembler.InvoiceModelAssembler;
import id.ac.arraniry.siserkomapi.assembler.KelasModelAssembler;
import id.ac.arraniry.siserkomapi.dto.*;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import id.ac.arraniry.siserkomapi.service.KelasService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kelas")
@CrossOrigin
public class KelasRest {
    private final KelasService kelasService;
    private final PagedResourcesAssembler<Kelas> pageResourceAssembler;
    private final KelasModelAssembler kelasModelAssembler;
    private final InvoiceModelAssembler invoiceModelAssembler;

    public KelasRest(KelasService kelasService, PagedResourcesAssembler<Kelas> pageResourceAssembler, KelasModelAssembler kelasModelAssembler,
                     InvoiceModelAssembler invoiceModelAssembler) {
        this.kelasService = kelasService;
        this.pageResourceAssembler = pageResourceAssembler;
        this.kelasModelAssembler = kelasModelAssembler;
        this.invoiceModelAssembler = invoiceModelAssembler;
    }

    @Operation(summary = "Mencari kelas")
    @GetMapping()
    public PagedModel<KelasModel> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "100") Integer size,
                                         @RequestParam(name = "jenisInvoice", required = false) Integer jenisInvoice) {
        if(size > 100) size = 100;
        Sort sort = Sort.by(
                Sort.Order.desc("tanggal"),
                Sort.Order.desc("jam"));
        Pageable paging = PageRequest.of(page, size, sort);
        var result = kelasService.findAll(paging);
        return pageResourceAssembler.toModel(result, kelasModelAssembler);
    }

    @Operation(summary = "Mendapatkan daftar kelas yang masih tersedia")
    @GetMapping("/available")
    public List<KelasModel> getJadwalAvailable(@RequestParam("jenisInvoice") Integer jenisInvoice) {
        var kelasList = kelasService.findAvailable(jenisInvoice);
        var kelasModelList = new ArrayList<KelasModel>();
        kelasList.forEach(k -> {
            kelasModelList.add(new KelasModel(k));
        });
        return kelasModelList;
    }

    @Operation(summary = "Mendapatkan kelas berdasarkan id kelas")
    @GetMapping("/{id}")
    public KelasModel getById(@PathVariable Integer id) {
        var dto = kelasService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kelas tidak ditemukan!"));
        return new KelasModel(dto);
    }

    @Operation(summary = "Mendapatkan daftar invoice berdasarkan id kelas")
    @GetMapping("/{id}/invoice")
    public CollectionModel<InvoiceModel> getInvoice(@PathVariable int id) {
        var inv = kelasService.findInvoiceByKelas(id);
        return invoiceModelAssembler.toCollectionModel(inv);
    }

    @Operation(summary = "Membuka kelas baru")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public KelasModel create(@Valid @RequestBody KelasCreateReq req) {
        return new KelasModel(kelasService.create(req));
    }

    @Operation(summary = "Menghapus invoice dari kelas")
    @DeleteMapping("/{id}/invoice/{noInvoice}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMahasiswaFromKelas(@PathVariable Integer id, @PathVariable String noInvoice, @Valid @RequestBody UpdatedByReq req){
        kelasService.deleteInvoiceFromKelas(id, noInvoice, req);
    }

    @Operation(summary = "Mengubah kelas")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateJadwal(@PathVariable Integer id, @Valid @RequestBody KelasUpdateReq req) {
        kelasService.updateById(id, req);
    }
}
