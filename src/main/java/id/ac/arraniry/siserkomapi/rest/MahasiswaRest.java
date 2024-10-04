package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.assembler.InvoiceModelAssembler;
import id.ac.arraniry.siserkomapi.assembler.MahasiswaModelAssembler;
import id.ac.arraniry.siserkomapi.dto.InvoiceModel;
import id.ac.arraniry.siserkomapi.dto.MahasiswaModel;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.service.InvoiceService;
import id.ac.arraniry.siserkomapi.service.MahasiswaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mahasiswa")
@CrossOrigin
public class MahasiswaRest {

    private final PagedResourcesAssembler<Mahasiswa> pagedResourcesAssembler;
    private final MahasiswaService mahasiswaService;
    private final MahasiswaModelAssembler mahasiswaModelAssembler;
    private final InvoiceService invoiceService;
    private final InvoiceModelAssembler invoiceModelAssembler;

    @Autowired
    public MahasiswaRest(MahasiswaModelAssembler customerModelAssembler, PagedResourcesAssembler<Mahasiswa> pagedResourcesAssembler,
                         MahasiswaService mahasiswaService, MahasiswaModelAssembler mahasiswaModelAssembler,
                         InvoiceService invoiceService, InvoiceModelAssembler invoiceModelAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.mahasiswaService = mahasiswaService;
        this.mahasiswaModelAssembler = mahasiswaModelAssembler;
        this.invoiceService = invoiceService;
        this.invoiceModelAssembler = invoiceModelAssembler;
    }

    @Operation(summary = "Mencari mahasiswa berdasarkan nim atau nama")
    @GetMapping()
    public PagedModel<MahasiswaModel> search(
            @RequestParam(defaultValue = "") String searchString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        if(size > 100) size = 100;
        var pageable = PageRequest.of(page, size, Sort.by("nama"));
        var result = mahasiswaService.findByIdOrNama(searchString, pageable);
        return pagedResourcesAssembler.toModel(result, mahasiswaModelAssembler);
    }

    @Operation(summary = "Mendapatkan mahasiswa berdasarkan nim")
    @GetMapping("/{nim}")
    public MahasiswaModel getMahasiswaByNim(@PathVariable String nim) {
        return mahasiswaService.findMahasiswaById(nim).map(MahasiswaModel::new).orElse(null);
//        try {
//            mhs = mahasiswaService.findMahasiswaById(nim);
//        } catch (ResponseStatusException e) {
//            mhs = mahasiswaService.syncMahasiswa(nim);
//        }
    }

    @Operation(summary = "Mendapatkan daftar invoice berdasarkan nim")
    @GetMapping("/{nim}/invoice")
    public CollectionModel<InvoiceModel> getInvoicesMahasiswa(@PathVariable String nim) {
        var inv = invoiceService.findByNim(nim);
        return invoiceModelAssembler.toCollectionModel(inv);
    }
}
