package id.ac.arraniry.siserkomapi.assembler;

import id.ac.arraniry.siserkomapi.dto.MataKuliahModel;
import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Controller;

@Controller
public class MataKuliahModelAssembler implements RepresentationModelAssembler<MataKuliah, MataKuliahModel> {
    @Override
    public MataKuliahModel toModel(MataKuliah entity) {
        return new MataKuliahModel(entity);
    }
}
