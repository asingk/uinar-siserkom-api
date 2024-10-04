package id.ac.arraniry.siserkomapi.assembler;

import id.ac.arraniry.siserkomapi.dto.KelasModel;
import id.ac.arraniry.siserkomapi.entity.Kelas;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class KelasModelAssembler implements RepresentationModelAssembler<Kelas, KelasModel> {
    @Override
    public KelasModel toModel(Kelas entity) {
        return new KelasModel(entity);
    }
}
