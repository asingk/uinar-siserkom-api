package id.ac.arraniry.siserkomapi.dto;

import id.ac.arraniry.siserkomapi.entity.MataKuliah;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class MataKuliahModel extends RepresentationModel<MataKuliahModel> {
    private String id;
    private String nama;
    private Boolean isDisabled;

    public MataKuliahModel(MataKuliah entity) {
        this.id = entity.getId();
        this.nama = entity.getNama();
        this.isDisabled = entity.getIsDisabled();
    }
}
