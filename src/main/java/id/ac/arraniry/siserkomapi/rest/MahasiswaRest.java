package id.ac.arraniry.siserkomapi.rest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jcraft.jsch.*;
import id.ac.arraniry.siserkomapi.assembler.InvoiceModelAssembler;
import id.ac.arraniry.siserkomapi.assembler.MahasiswaModelAssembler;
import id.ac.arraniry.siserkomapi.dto.InvoiceModel;
import id.ac.arraniry.siserkomapi.dto.MahasiswaModel;
import id.ac.arraniry.siserkomapi.dto.MataKuliahMahasiswaModel;
import id.ac.arraniry.siserkomapi.entity.Mahasiswa;
import id.ac.arraniry.siserkomapi.service.InvoiceService;
import id.ac.arraniry.siserkomapi.service.MahasiswaService;
import id.ac.arraniry.siserkomapi.util.GlobalConstants;
import io.swagger.v3.oas.annotations.Operation;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Random;

@RestController
@RequestMapping("/mahasiswa")
@CrossOrigin
public class MahasiswaRest {

    private static final Logger log = LoggerFactory.getLogger(MahasiswaRest.class);

    private static final String SAVE_FOLDER = File.separator + "var" + File.separator + "tmp";
//    private static final String CDN_HOST = "192.168.176.227";
    private static final String CDN_HOST = "103.107.187.227";
    private static final String CDN_HOME_FOLDER = "/home/sertsftp/data";
    private static final String CDN_USERNAME = "sertsftp";
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
    // env prod
//    private static final String CDN_SISERKOM_SERTIFIKAT_FOLDER = "/siserkom/sertifikat";
    // env staging
	private static final String CDN_SISERKOM_SERTIFIKAT_FOLDER = "/siserkom/staging_sertifikat";

    private final PagedResourcesAssembler<Mahasiswa> pagedResourcesAssembler;
    private final MahasiswaService mahasiswaService;
    private final MahasiswaModelAssembler mahasiswaModelAssembler;
    private final InvoiceService invoiceService;
    private final InvoiceModelAssembler invoiceModelAssembler;
    private final Environment environment;

    @Autowired
    public MahasiswaRest(PagedResourcesAssembler<Mahasiswa> pagedResourcesAssembler,
                         MahasiswaService mahasiswaService, MahasiswaModelAssembler mahasiswaModelAssembler,
                         InvoiceService invoiceService, InvoiceModelAssembler invoiceModelAssembler, Environment environment) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.mahasiswaService = mahasiswaService;
        this.mahasiswaModelAssembler = mahasiswaModelAssembler;
        this.invoiceService = invoiceService;
        this.invoiceModelAssembler = invoiceModelAssembler;
        this.environment = environment;
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
    public MahasiswaModel get(@PathVariable String nim) {
        return mahasiswaService.findById(nim).map(MahasiswaModel::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa tidak ditemukan!"));
    }

    @Operation(summary = "Menambah mahasiswa berdasarkan nim")
    @PostMapping("/{nim}")
    @ResponseStatus(HttpStatus.CREATED)
    public MahasiswaModel sync(@PathVariable String nim) {
        var opt = mahasiswaService.findById(nim);
        if (opt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mahasiswa sudah ada!");
        }
        return mahasiswaModelAssembler.toModel(mahasiswaService.sync(nim));
    }

    @Operation(summary = "Mendapatkan daftar invoice berdasarkan nim")
    @GetMapping("/{nim}/invoice")
    public CollectionModel<InvoiceModel> getInvoicesMahasiswa(@PathVariable String nim) {
        var inv = invoiceService.findByNim(nim);
        return invoiceModelAssembler.toCollectionModel(inv);
    }

    @Operation(summary = "Membuat invoice baru")
    @PostMapping("/{nim}/invoice")
    public InvoiceModel createInvoice(
            @PathVariable String nim,
            @RequestParam("jenisInvoice") Integer jenisInvoice) {
        return new InvoiceModel(invoiceService.create(nim, jenisInvoice));
    }

    @Operation(summary = "Mendapatkan nilai mata kuliah berdasarkan nim")
    @GetMapping("/{nim}/matakuliah")
    public List<MataKuliahMahasiswaModel> getMataKuliah(@PathVariable String nim ) {
        var mhs = mahasiswaService.findById(nim).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa tidak ditemukan!"));
        List<MataKuliahMahasiswaModel> mataKuliahMahasiswaModelList = new ArrayList<>();
        mhs.getMataKuliah().forEach(row -> mataKuliahMahasiswaModelList.add(new MataKuliahMahasiswaModel(row)));
        return mataKuliahMahasiswaModelList;
    }

    @Operation(summary = "Update nilai mata kuliah berdasarkan nim")
    @PostMapping("/{nim}/matakuliah")
    public List<MataKuliahMahasiswaModel> syncMataKuliah(@PathVariable String nim ) {
        mahasiswaService.clearMataKuliahMahasiswa(nim);
        List<MataKuliahMahasiswaModel> mataKuliahMahasiswaModelList = new ArrayList<>();
        var matkulMhs = mahasiswaService.syncMataKuliah(nim);
        matkulMhs.forEach(row -> mataKuliahMahasiswaModelList.add(new MataKuliahMahasiswaModel(row)));
        return mataKuliahMahasiswaModelList;
    }

    @Operation(summary = "Mencetak sertifikat")
    @PostMapping("/{nim}/sertifikat")
    public MahasiswaModel createCertificate(
            @PathVariable String nim ) throws JRException, IOException, WriterException {
        /* testing purpose only */
        //		String nim = "160503082";
        //		MahasiswaDto mahasiswa = mahasiswaService.findById(nim);
        Mahasiswa mahasiswa = mahasiswaService.findById(nim)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Mahasiswa tidak ditemukan!"));
        if(null != mahasiswa.getCertificateNo() && !mahasiswa.getCertificateNo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda sudah memiliki sertifikat!");
        }
        InputStream reportStream;
        if (mahasiswa.getIsLulusMatkul()) {
            invoiceService.findByMahasiswaIdAndJenisInvoiceIdAndIsSudahBayar(nim, GlobalConstants.JENIS_INVOICE_BUAT_SERTIFIKAT, true)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Silakan bayar tagihan buat sertifikat terlebih dahulu!"));
            reportStream = getClass().getResourceAsStream("/siserkom_2.jasper");
        } else {
            invoiceService.findByMahasiswaIdAndIsLulusUjian(nim, true)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda belum lulus!"));
            reportStream = getClass().getResourceAsStream("/siserkom_1.jasper");
        }
        String filename = generateRandomName();
        generateReport(filename, mahasiswa, reportStream);
        mahasiswa.setCertificateNo(filename);
        mahasiswa.setUpdatedAt(LocalDateTime.now());
        return new MahasiswaModel(mahasiswaService.save(mahasiswa));
    }

    private String generateRandomName() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 12;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void generateReport(String filename, Mahasiswa mahasiswa, InputStream reportStream) throws JRException, IOException, WriterException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tanggal", new Date());
        parameters.put("nim", mahasiswa.getId());
        parameters.put("nama", convertToTitleCaseIteratingChars(mahasiswa.getNama()));
        parameters.put("qrcode", generateQRCode(filename));
        parameters.put("verifikasiUrl", GlobalConstants.SERTIFIKAT_URL + "/" + filename);
        parameters.put("nomor", "Un.08/PTIPD/PP.00.9/" + filename + "/" + LocalDate.now().getYear());

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, new JREmptyDataSource() );
        extractPrintImage(filename, jasperPrint);
        uploadFile(filename);
        Files.delete(Paths.get(SAVE_FOLDER + File.separator + filename + ".png"));
        Files.delete(Paths.get(SAVE_FOLDER + File.separator + filename + ".jpg"));
    }

    private static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    private String generateQRCode(String filename) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(GlobalConstants.SERTIFIKAT_URL + "/" + filename, BarcodeFormat.QR_CODE, 200, 200);
        Path path = Paths.get(SAVE_FOLDER + File.separator + filename + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return path.toString();
    }

    private void extractPrintImage (String filename, JasperPrint print){
        File file = new File(SAVE_FOLDER + File.separator + filename + ".jpg");
        OutputStream ouputStream;
        try {
            ouputStream= new FileOutputStream(file);
            BufferedImage renderedImage = (BufferedImage) JasperPrintManager.printPageToImage(print, 0, 1.6f);
            ImageIO.write(renderedImage, "jpg", ouputStream);

        } catch(Exception e) {
//			e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void uploadFile(String filename) {
        log.debug("---uploadFile start---");

        String localFile = SAVE_FOLDER + File.separator + filename;
        String remoteDir = CDN_HOME_FOLDER + CDN_SISERKOM_SERTIFIKAT_FOLDER + File.separator;
        Session jschSession = null;
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/asingk/.ssh/known_hosts");
            jschSession = jsch.getSession(CDN_USERNAME, CDN_HOST);
            jschSession.setPassword(environment.getProperty("env.data.cdn-password"));
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            channelSftp.put(localFile + ".jpg", remoteDir + filename + ".jpg");
            channelSftp.exit();
        } catch (JSchException | SftpException e) {
//			e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
    }
}
