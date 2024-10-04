package id.ac.arraniry.siserkomapi.assembler;

import id.ac.arraniry.siserkomapi.dto.InvoiceModel;
import id.ac.arraniry.siserkomapi.entity.Invoice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceModelAssembler implements RepresentationModelAssembler<Invoice, InvoiceModel> {
    @Override
    public InvoiceModel toModel(Invoice entity) {
        return new InvoiceModel(entity);
    }
    @Override
    public CollectionModel<InvoiceModel> toCollectionModel(Iterable<? extends Invoice> entities) {
        List<InvoiceModel> models = new ArrayList<>();
        for (Invoice entity : entities) {
            models.add(toModel(entity));
        }
        return CollectionModel.of(models);
    }
}
