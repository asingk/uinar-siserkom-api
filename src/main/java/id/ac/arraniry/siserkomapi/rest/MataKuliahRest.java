package id.ac.arraniry.siserkomapi.rest;

import id.ac.arraniry.siserkomapi.assembler.MataKuliahModelAssembler;
import id.ac.arraniry.siserkomapi.dto.MataKuliahCreateReq;
import id.ac.arraniry.siserkomapi.dto.MataKuliahModel;
import id.ac.arraniry.siserkomapi.dto.MataKuliahUpdateReq;
import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import id.ac.arraniry.siserkomapi.service.MataKuliahService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/mata-kuliah")
@CrossOrigin
public class MataKuliahRest {
    private final MataKuliahService matkulService;
    private final PagedResourcesAssembler<MataKuliah> pagedResourcesAssembler;
    private final MataKuliahModelAssembler mataKuliahModelAssembler;

    public MataKuliahRest(MataKuliahService matkulService, PagedResourcesAssembler<MataKuliah> pagedResourcesAssembler, MataKuliahModelAssembler mataKuliahModelAssembler) {
        this.matkulService = matkulService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.mataKuliahModelAssembler = mataKuliahModelAssembler;
    }

    @GetMapping()
    public PagedModel<MataKuliahModel> getAll(
            @RequestParam(defaultValue = "") String searchString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        if(size > 100) size = 100;
        var pageable = PageRequest.of(page, size, Sort.by("nama"));
        var mk = matkulService.findAllCollectionModel(searchString, searchString, pageable);
        return pagedResourcesAssembler.toModel(mk, mataKuliahModelAssembler);
    }

    @GetMapping("/{id}")
    public MataKuliah get(@PathVariable String id) {
        return matkulService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mata Kuliah tidak ditemukan!"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MataKuliah create(@Valid @RequestBody MataKuliahCreateReq req) {
        return matkulService.create(req);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @Valid @RequestBody MataKuliahUpdateReq req) {
        matkulService.update(id, req);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        matkulService.delete(id);
    }
}
