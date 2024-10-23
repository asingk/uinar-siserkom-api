package id.ac.arraniry.siserkomapi.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class InvoiceSevimaRespDataAttributes {
    private String id_jenis_akun;
    private String kode_transaksi;
    private String is_lunas;
    private ZonedDateTime tanggal_transaksi;
}
