package id.ac.arraniry.siserkomapi.assembler;

import id.ac.arraniry.siserkomapi.dto.MahasiswaModel;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class MahasiswaModelAssembler implements RepresentationModelAssembler<Mahasiswa, MahasiswaModel> {
    @Override
    public MahasiswaModel toModel(Mahasiswa entity) {
        return new MahasiswaModel(entity);
    }
}
