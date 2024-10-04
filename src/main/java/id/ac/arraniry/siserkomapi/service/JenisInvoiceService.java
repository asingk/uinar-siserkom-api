package id.ac.arraniry.siserkomapi.service;

import id.ac.arraniry.siserkomapi.dto.JenisInvoiceUpdateReq;
import id.ac.arraniry.siserkomapi.entity.JenisInvoice;

import java.util.List;
import java.util.Optional;

public interface JenisInvoiceService {
    List<JenisInvoice> findAll();
    Optional<JenisInvoice> findById(Integer id);
    void update(Integer id, JenisInvoiceUpdateReq req);
}
