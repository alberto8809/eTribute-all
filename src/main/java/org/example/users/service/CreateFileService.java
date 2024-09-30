package org.example.users.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.users.model.*;
import org.example.users.repository.*;
import org.example.users.util.CreateFilePDFPolicy;
import org.example.users.util.ParserFileEgresos;
import org.example.users.util.ParserFileIngresos;
import org.example.users.util.UploadFileToS3_Policies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class CreateFileService {
    // private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all/";
    private static final String server_path = "/home/ubuntu/endpoints/eTribute-all/";
    public static final Logger LOGGER = LogManager.getLogger(CreateFileService.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClaveProductoServRepository claveProductoServRepository;
    @Autowired
    private CuentaContableRepository cuentaContableRepository;
    @Autowired
    private MethodOfPaymentRepository methodOfPaymentRepository;
    @Autowired
    private SaveObjRepository saveObjRepository;


    public CreateFileService() {
    }

    public CreateFileService(AccountRepository accountRepository, ClaveProductoServRepository claveProductoServRepository, CuentaContableRepository cuentaContableRepository, MethodOfPaymentRepository methodOfPaymentRepository, SaveObjRepository saveObjRepository) {
        this.accountRepository = accountRepository;
        this.claveProductoServRepository = claveProductoServRepository;
        this.cuentaContableRepository = cuentaContableRepository;
        this.methodOfPaymentRepository = methodOfPaymentRepository;
        this.saveObjRepository = saveObjRepository;
    }

    public List<String> getClaveProductoService(List<String> c_claveprodserv, String type, List<String> nomine) {
        List<String> claveProductoServs = new ArrayList<>();

        for (String clv : c_claveprodserv) {

            if (type.equals("E")) {
                claveProductoServs.add(claveProductoServRepository.getClaveProductoS("01010101"));
            } else if (type.equals("N")) {

                claveProductoServs.add(claveProductoServRepository.getClaveProductoS(clv));
                for (String value : nomine) {

                    claveProductoServs.add(methodOfPaymentRepository.getCuentaContableByNomina(value));

                }

            } else {
                claveProductoServs.add(claveProductoServRepository.getClaveProductoS(clv));

            }
        }

        return claveProductoServs;
    }


    public List<String> getCuentaCobtableList(List<String> claveProductoServ) {
        List<String> account = new ArrayList<>();

        for (String clv : claveProductoServ) {
            account.add(cuentaContableRepository.getCuantaContableMethod(clv));
        }

        return account;
    }

    public List<String> getIvaIeps(Map<String, String> claveProductoServ, String type, String amount) {
        List<String> iva = new ArrayList<>();

        if (type.equals("P")) {
            for (String clv : claveProductoServ.keySet()) {
                iva.add(methodOfPaymentRepository.getCuentaContableByTax(clv));
            }
        } else {

            for (String clv : claveProductoServ.keySet()) {
                iva.add(methodOfPaymentRepository.getCuentaContableByTax(clv));
            }
        }
        return iva;
    }


    public boolean uploadToS3(MultipartFile[] files, String rfc) throws IOException {
        if (files.length < 0) {
            throw new RuntimeException("Uploaded file is empty");
        }

        String fileName = "";
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename();
            File uploadDir = new File(server_path + rfc + "/xml");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Path filePath = Paths.get(server_path + rfc + "/xml", fileName);
            Files.write(filePath, file.getBytes());

            UploadFileToS3_Policies.upload(fileName, rfc);

        }
        return true;
    }


    public Map<String, Object> getDescriptionPolicy(String rfc, String initial_date, String final_date) {

        Map<String, Object> filesXMLFromAWS = UploadFileToS3_Policies.getFileFromAWS(rfc, "xml", initial_date, final_date);
        Map<String, Object> finalResult = new HashMap<>();

        int value = createPolicy(rfc, "EGRESOS", initial_date, final_date);
        if (value == 1) {

            Map<String, Object> filesPDFromAWS = UploadFileToS3_Policies.getFileFromAWS(rfc, "pdf", initial_date, final_date);

            List<Response> eg = (List<Response>) filesXMLFromAWS.get("Emitidas");
            List<Response> rv = (List<Response>) filesXMLFromAWS.get("Recibidas");
            Map<String, String> obj3 = (Map<String, String>) filesPDFromAWS.get("listOfEgresospdf");
            Map<String, String> obj4 = (Map<String, String>) filesPDFromAWS.get("listOfIngresospdf");

            for (Map.Entry<String, String> r : obj3.entrySet()) {
                String filePDF = r.getKey().replace(".pdf", "");
                for (Response r1 : eg) {
                    if (r1.getUrl_xml().contains(filePDF)) {
                        r1.setUrl_pdf(r.getValue());
                    }
                }

            }

            for (Map.Entry<String, String> r : obj4.entrySet()) {
                String filePDF = r.getKey().replace(".pdf", "");
                for (Response rd : rv) {
                    if (rd.getUrl_xml().contains(filePDF)) {
                        rd.setUrl_pdf(r.getValue());
                    }
                }

            }

            eg.removeIf(n -> n.getUrl_pdf() == null);
            rv.removeIf(n -> n.getUrl_pdf() == null);


            finalResult.put("Emitidas", filesXMLFromAWS.get("Emitidas"));
            finalResult.put("Recibidas", filesXMLFromAWS.get("Recibidas"));

        } else if (value == 2) {
            createPolicy(rfc, "INGRESOS", initial_date, final_date);

            Map<String, Object> filesPDFromAWS = UploadFileToS3_Policies.getFileFromAWS(rfc, "pdf", initial_date, final_date);

            List<Response> eg = (List<Response>) filesXMLFromAWS.get("Emitidas");
            List<Response> rv = (List<Response>) filesXMLFromAWS.get("Recibidas");
            Map<String, String> obj3 = (Map<String, String>) filesPDFromAWS.get("listOfEgresospdf");
            Map<String, String> obj4 = (Map<String, String>) filesPDFromAWS.get("listOfIngresospdf");

            for (Map.Entry<String, String> r : obj3.entrySet()) {
                String filePDF = r.getKey().replace(".pdf", "");
                for (Response r1 : eg) {
                    if (r1.getUrl_xml().contains(filePDF)) {
                        r1.setUrl_pdf(r.getValue());
                    }
                }

            }

            for (Map.Entry<String, String> r : obj4.entrySet()) {
                String filePDF = r.getKey().replace(".pdf", "");
                for (Response rd : rv) {
                    if (rd.getUrl_xml().contains(filePDF)) {
                        rd.setUrl_pdf(r.getValue());
                    }
                }

            }


            eg.removeIf(n -> n.getUrl_pdf() == null);
            rv.removeIf(n -> n.getUrl_pdf() == null);


            finalResult.put("Emitidas", filesXMLFromAWS.get("Recibidas"));
            finalResult.put("Recibidas", filesXMLFromAWS.get("Emitidas"));


        }
        return finalResult;
    }


    public int createPolicy(String rfc, String type, String initial_date, String final_date) {
        try {
            Random rand = new Random();
            int account_id = accountRepository.getAccountByAccount_id(rfc);
            List<CuentaContable> cuentaContable = new ArrayList<>();
            File folder = new File(server_path + rfc + "/xml/" + type + "/");
            File[] listOfFiles = folder.listFiles();
            List<String> objFromDB = saveObjRepository.getObjFromDB(rfc, type, initial_date, final_date);

            if (!objFromDB.isEmpty()) {
                if (Arrays.stream(listOfFiles).anyMatch(n -> n.getName().equals(objFromDB.get(0)))) {
                    return 1;
                }
            } else {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        if (file.getAbsolutePath().contains("EGRESOS")) {

                            PolicyObjFile policyObjFile = ParserFileEgresos.getParse(server_path + rfc + "/xml/" + type + "/" + file.getName());
                            if (policyObjFile != null) {
                                if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("P")) {
                                    policyObjFile.setCuenta_method("102.01");
                                    policyObjFile.setDescription_methods(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getCuenta_method()));
                                    List<String> id = new ArrayList<>();
                                    id.add("209.01");
                                    id.add("105.99");
                                    policyObjFile.setTax_id(id);

                                    List<String> desc = new ArrayList<>();
                                    desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));
                                    desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(1)));
                                    policyObjFile.setTax_description(desc);
                                    policyObjFile.getPolicyObj().setVenta_id("208.01");
                                    policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));

                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {

                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                        for (int i = 0; i < taxId.size(); i++) {

                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }
                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }

                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getFolio());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);

                                        }
                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }

                                } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("I") && policyObjFile.getPolicyObj().getMethodPayment().equals("PUE")) {

                                    List<String> claveProductoServ = getClaveProductoService(policyObjFile.getPolicyObj().getClaveProdServ(), policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getTraslado());
                                    cuentaContable.add(cuentaContableRepository.getCuantaContable(policyObjFile.getPolicyObj().getVenta_id()));
                                    policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));

                                    cuentaContable.add(cuentaContableRepository.getCuantaContable(claveProductoServ.isEmpty() ? "01" : claveProductoServ.get(0)));

                                    policyObjFile.setCuenta_method("118.01");
                                    policyObjFile.setDescription_methods(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getCuenta_method()));
                                    List<String> id = new ArrayList<>();
                                    id.add("118.03");
                                    id.add(policyObjFile.getCuenta_method());
                                    policyObjFile.setTax_id(id);


                                    List<String> desc = new ArrayList<>();
                                    desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));
                                    desc.add(policyObjFile.getDescription_methods());
                                    policyObjFile.setTax_description(desc);
                                    List<String> cargo = new ArrayList<>();
                                    if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                        List<String> abonos = policyObjFile.getPolicyObj().getRetencion_importe();
                                        if (abonos != null) {
                                            for (String clv : abonos) {
                                                cargo.add(cuentaContableRepository.getCuantaContableMethod(clv));
                                            }
                                        }
                                    }
                                    policyObjFile.getPolicyObj().setCargo(cargo);
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));
                                    policyObjFile.getPolicyObj().setConcepto_Descripcion(cuentaContableRepository.getCuantaContableMethod(claveProductoServ.get(0)));
                                    policyObjFile.setCuenta(claveProductoServ.get(0));


                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {

                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                        for (int i = 0; i < taxId.size(); i++) {
                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }

                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }

                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);
                                        }
                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }
                                } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("I") && policyObjFile.getPolicyObj().getMethodPayment().equals("PPD")) {

                                    policyObjFile.setCuenta_method("119.01");
                                    policyObjFile.setDescription_methods(cuentaContableRepository.getCuantaContableMethod(policyObjFile.getCuenta_method()));
                                    List<String> id = new ArrayList<>();
                                    id.add("119.03");
                                    policyObjFile.setTax_id(id);
                                    List<String> desc = new ArrayList<>();
                                    desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));
                                    policyObjFile.setTax_description(desc);
                                    policyObjFile.getPolicyObj().setVenta_id("209.01");
                                    policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));

                                    if (policyObjFile.getPolicyObj().getIva() != null) {
                                        Map<String, String> i = policyObjFile.getPolicyObj().getIva();
                                        Map<String, String> result = new HashMap<>();
                                        List<String> retencion = new ArrayList<>();

                                        for (Map.Entry<String, String> r : i.entrySet()) {
                                            if (r.getKey().equals("001")) {
                                                String c = cuentaContableRepository.getCuentaContableVenta("216.04");
                                                result.put("216.04", c);
                                                retencion.add(r.getValue());

                                            }
                                            if (r.getKey().equals("002")) {
                                                String c1 = cuentaContableRepository.getCuentaContableVenta("216.10");
                                                result.put("216.10", c1);
                                                retencion.add(r.getValue());

                                            }
                                        }

                                        policyObjFile.getPolicyObj().setRetencion_importe(retencion);
                                        policyObjFile.getPolicyObj().setIva(result);

                                    }
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));
                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                        for (int i = 0; i < taxId.size(); i++) {

                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }
                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }
                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);
                                        }


                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        //double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble("0");
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }
                                } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("E")) {

                                    policyObjFile.setCuenta_method("402.01");
                                    policyObjFile.setDescription_methods(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getCuenta_method()));
                                    List<String> id = new ArrayList<>();
                                    id.add("105.99");
                                    policyObjFile.setTax_id(id);
                                    List<String> desc = new ArrayList<>();
                                    desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));

                                    policyObjFile.setTax_description(desc);
                                    policyObjFile.getPolicyObj().setVenta_id("209.01");
                                    policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));
                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                        for (int i = 0; i < taxId.size(); i++) {
                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }
                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }

                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);
                                        }
                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }
                                } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("N")) {

                                    List<String> claveProductoServ = getClaveProductoService(policyObjFile.getPolicyObj().getClaveProdServ(), policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getTraslado());
                                    List<String> accounts = getCuentaCobtableList(claveProductoServ);

                                    //method payment (Abono)
                                    policyObjFile.setCuenta_method(methodOfPaymentRepository.getCuentaContable(policyObjFile.getPolicyObj().getTypeOfPayment()));
                                    policyObjFile.setDescription_methods(cuentaContableRepository.getCuantaContableMethod(policyObjFile.getCuenta_method()));

                                    claveProductoServ.add(policyObjFile.getCuenta_method());
                                    accounts.add(policyObjFile.getDescription_methods());
                                    policyObjFile.setTax_id(claveProductoServ);
                                    policyObjFile.setTax_description(accounts);
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));

                                    policyObjFile.getPolicyObj().setConcepto_Descripcion(cuentaContableRepository.getCuantaContableMethod(claveProductoServ.get(0)));
                                    policyObjFile.setCuenta(claveProductoServ.get(0));
                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {

                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());
                                        for (int i = 0; i < taxId.size(); i++) {
                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }
                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }
                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);
                                        }
                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }

                                } else {
                                    // (Cargo)
                                    policyObjFile.setTax_id(getIvaIeps(policyObjFile.getPolicyObj().getIva(), policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getAmount()));
                                    policyObjFile.setTax_description(getCuentaCobtableList(policyObjFile.getTax_id()));
                                    int rand_int1 = rand.nextInt(1000000000);
                                    policyObjFile.setFolio(String.valueOf(rand_int1));
                                    if (CreateFilePDFPolicy.makeFileEgreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                                        List<String> taxId = policyObjFile.getTax_id();
                                        taxId.add(policyObjFile.getCuenta_method());
                                        policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                        for (int i = 0; i < taxId.size(); i++) {
                                            PolicytoDB policytoDB = new PolicytoDB();
                                            policytoDB.setCliente(policyObjFile.getClient());
                                            policytoDB.setUsuario(rfc);
                                            policytoDB.setTipo(type);
                                            policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                            policytoDB.setPoliza(policyObjFile.getFolio());
                                            policytoDB.setCuenta(Double.parseDouble(taxId.get(i)));
                                            policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                            double sumDebe = 0;
                                            if (debe != null) {
                                                for (String db : debe) {
                                                    sumDebe += Double.parseDouble(db);

                                                }
                                            }
                                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                            if (policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                                                List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                                double sumHaber = 0;
                                                if (haber != null) {
                                                    for (String db : haber) {
                                                        sumHaber += Double.parseDouble(db);

                                                    }
                                                }
                                            }

                                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                            policytoDB.setFecha(policyObjFile.getDate());
                                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                            policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                            policytoDB.setSaldo_inicial("0");
                                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                            policytoDB.setSaldo_final(String.valueOf(fin));
                                            policytoDB.setAccount_id(account_id);
                                            policytoDB.setFile_name(file.getName());
                                            saveObjRepository.save(policytoDB);
                                        }
                                        PolicytoDB policyAccountDB = new PolicytoDB();
                                        policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                        policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                        policyAccountDB.setCliente(policyObjFile.getClient());
                                        policyAccountDB.setUsuario(rfc);
                                        policyAccountDB.setTipo(type);
                                        policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                        policyAccountDB.setPoliza(policyObjFile.getFolio());
                                        policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                        //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                        policyAccountDB.setHaber("0");
                                        policyAccountDB.setFecha(policyObjFile.getDate());
                                        policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                        policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                        policyAccountDB.setSaldo_inicial("0");
                                        double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                        policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                        policyAccountDB.setAccount_id(account_id);
                                        policyAccountDB.setFile_name(file.getName());
                                        saveObjRepository.save(policyAccountDB);
                                    }
                                }
                            }
                        } else if (file.getAbsolutePath().contains("INGRESOS")) {

                            PolicyObjFile policyObjFile = ParserFileIngresos.getParse(server_path + rfc + "/xml/" + type + "/" + file.getName());
                            List<String> claveProductoServ = getClaveProductoService(policyObjFile.getPolicyObj().getClaveProdServ(), policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getTraslado());
                            List<String> accounts = getCuentaCobtableList(claveProductoServ);


                            // method payment (Abono)

                            policyObjFile.setCuenta_method(methodOfPaymentRepository.getCuentaContable(policyObjFile.getPolicyObj().getTypeOfPayment()));
                            policyObjFile.setDescription_methods(cuentaContableRepository.getCuantaContableMethod(policyObjFile.getCuenta_method()));
                            claveProductoServ.add(policyObjFile.getCuenta_method());
                            accounts.add(policyObjFile.getDescription_methods());
                            policyObjFile.setTax_id(claveProductoServ);
                            policyObjFile.setTax_description(accounts);
                            policyObjFile.setCuenta(claveProductoServ.get(0));
                            int rand_int1 = rand.nextInt(1000000000);
                            policyObjFile.setFolio(String.valueOf(rand_int1));
                            if (CreateFilePDFPolicy.makeFileIngreso(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                                List<String> taxId = policyObjFile.getTax_id();
                                taxId.add(policyObjFile.getCuenta_method());
                                policyObjFile.getTax_description().add(policyObjFile.getDescription_methods());

                                for (int i = 0; i < taxId.size(); i++) {
                                    String taxIdValue = taxId.get(i);
                                    PolicytoDB policytoDB = new PolicytoDB();
                                    policytoDB.setCliente(policyObjFile.getClient());
                                    policytoDB.setUsuario(rfc);
                                    policytoDB.setTipo(type);
                                    policytoDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                    policytoDB.setPoliza(policyObjFile.getFolio());
                                    try {
                                        // Extraer el último valor numérico
                                        if (taxIdValue != null && !taxIdValue.trim().isEmpty()) {
                                            // Dividir la cadena en partes basadas en ","
                                            String[] parts = taxIdValue.split(",");

                                            // Tomar el último valor
                                            String lastValue = parts[parts.length - 1].trim();

                                            // Intentar convertir el último valor a double
                                            double cuenta = Double.parseDouble(lastValue);
                                            policytoDB.setCuenta(cuenta);
                                            // LOGGER.info("Set cuenta: {}", cuenta);
                                        } else {
                                            // Manejar caso donde el valor es nulo o vacío
                                            //LOGGER.warn("taxId value at index {} is null or empty, setting cuenta to 0.0", i);
                                            policytoDB.setCuenta(0.0);
                                        }
                                    } catch (NumberFormatException e) {
                                        // Manejar caso donde la conversión falla
                                        LOGGER.error("Error parsing taxId value at index {}: {}", i, taxIdValue, e);
                                        policytoDB.setCuenta(0.0); // O cualquier valor predeterminado que consideres adecuado
                                    }
                                    policytoDB.setDescripcion(policyObjFile.getTax_description().get(i));

                                    List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                                    double sumDebe = 0;
                                    if (debe != null) {
                                        for (String db : debe) {
                                            sumDebe += Double.parseDouble(db);

                                        }
                                    }
                                    policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());

                                    List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                                    double sumHaber = 0;
                                    if (haber != null) {
                                        for (String db : haber) {
                                            sumHaber += Double.parseDouble(db);

                                        }
                                    }
                                    policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                    policytoDB.setFecha(policyObjFile.getDate());
                                    policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                    policytoDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                    policytoDB.setSaldo_inicial("0");
                                    double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                                    policytoDB.setSaldo_final(String.valueOf(fin));
                                    policytoDB.setAccount_id(account_id);
                                    policytoDB.setFile_name(file.getName());
                                    saveObjRepository.save(policytoDB);
                                }
                                PolicytoDB policyAccountDB = new PolicytoDB();
                                policyAccountDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta()));
                                policyAccountDB.setDescripcion(policyObjFile.getCuenta().equals("601.34") ? "Honorarios a personas físicas residentes nacionales" : policyObjFile.getPolicyObj().getConcepto_Descripcion());
                                policyAccountDB.setCliente(policyObjFile.getClient());
                                policyAccountDB.setUsuario(rfc);
                                policyAccountDB.setTipo(type);
                                policyAccountDB.setProveedor(policyObjFile.getPolicyObj().getCompanyName());
                                policyAccountDB.setPoliza(policyObjFile.getFolio());
                                policyAccountDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                                //policyAccountDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                                policyAccountDB.setHaber("0");
                                policyAccountDB.setFecha(policyObjFile.getDate());
                                policyAccountDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                                policyAccountDB.setTotal(policyObjFile.getPolicyObj().getAmount());
                                policyAccountDB.setSaldo_inicial("0");
                                double fin2 = Double.parseDouble(policyAccountDB.getSaldo_inicial()) + Double.parseDouble(policyAccountDB.getTotal());
                                policyAccountDB.setSaldo_final(String.valueOf(fin2));
                                policyAccountDB.setAccount_id(account_id);
                                policyAccountDB.setFile_name(file.getName());
                                saveObjRepository.save(policyAccountDB);

                            }
                        }
                    }
                }
                return 2;
            }
        } catch (Exception e) {
            LOGGER.error(" createPolicy - createFile :   " + e.getMessage() + e.getCause() + e.getLocalizedMessage());
        }
        return 0;
    }


}


