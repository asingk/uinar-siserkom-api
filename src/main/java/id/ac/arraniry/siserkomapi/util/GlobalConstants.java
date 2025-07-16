package id.ac.arraniry.siserkomapi.util;

public class GlobalConstants {
    public static final Integer JENIS_INVOICE_PELATIHAN = 1;
    public static final Integer JENIS_INVOICE_UJIAN = 2;
    public static final Integer JENIS_INVOICE_BUAT_SERTIFIKAT = 3;
    public static final String[] NILAI_MK_LULUS = {"A", "A-", "B+", "B", "B-", "C+", "C"};
    public static final String SIAKAD_API_URL = "https://api.sevimaplatform.com/siakadcloud/v1";
    public static final String SEVIMA_JENIS_INVOICE_PELATIHAN = "018";
    public static final String SEVIMA_JENIS_INVOICE_UJIAN = "013";
    public static final String SEVIMA_JENIS_INVOICE_BUAT_SERTIFIKAT = "014";
    public static final String CDN_HOST = "192.168.176.26";
    public static final String CDN_HOME_FOLDER = "/usr/share/nginx/cdn";
    public static final String CDN_USERNAME = "sertsftp";

//     env staging
//	  public static final String SERTIFIKAT_URL = "http://192.168.176.23:3200/siserkom";
//    public static final String CDN_SISERKOM_SERTIFIKAT_FOLDER = "/siserkom/staging_sertifikat";

    // env prod
    public static final String SERTIFIKAT_URL = "https://serdig.ar-raniry.ac.id/siserkom";
    public static final String CDN_SISERKOM_SERTIFIKAT_FOLDER = "/siserkom/sertifikat";
}
