package org.example.users.service;


import org.example.users.model.*;
import org.example.users.repository.*;
import org.example.users.util.CreateFilePDFPolicy;
import org.example.users.util.ParserFile;
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
    // private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all3/";
    private static String server_path = "/home/ubuntu/endpoints/eTribute-all/";

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
        if (c_claveprodserv != null || !c_claveprodserv.isEmpty()) {
            for (String clv : c_claveprodserv) {

                if (type.equals("E")) {
                    List<String> clave = claveProductoServRepository.getClaveProductoS("01010101");
                    for (String cl : clave) {
                        claveProductoServs.add(cl);
                    }
                } else if (type.equals("N")) {
                    //query to get accounts
                    claveProductoServs.add(methodOfPaymentRepository.getCuentaContablePer(clv));
                    for (String value : nomine) {
                        claveProductoServs.add(methodOfPaymentRepository.getCuentaContableByNomina(value));

                    }

                } else {

                    List<String> clave = claveProductoServRepository.getClaveProductoS(clv);
                    for (String cl : clave) {
                        claveProductoServs.add(cl);
                    }


                }
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

            //date is comming from xml
            UploadFileToS3_Policies.upload(fileName, rfc);

        }
        return true;
    }


    public Map<String, Object> getDescriptionPolicy(String rfc, String initial_date, String final_date) {

        Map<String, Object> filesXMLFromAWS = UploadFileToS3_Policies.getFileFromAWS(rfc, "xml", initial_date, final_date);
        createPolicy(rfc, "EGRESOS");
        createPolicy(rfc, "INGRESOS");
        Map<String, Object> filesPDFromAWS = UploadFileToS3_Policies.getFileFromAWS(rfc, "pdf", initial_date, final_date);
        Map<String, Object> finalResult = new HashMap<>();

        List<Response> eg = (List<Response>) filesXMLFromAWS.get("Emitidas");
        List<Response> rv = (List<Response>) filesXMLFromAWS.get("Recibidas");
        Map<String, String> obj = (Map<String, String>) filesXMLFromAWS.get("listOfEgresosxml");
        Map<String, String> obj1 = (Map<String, String>) filesXMLFromAWS.get("listOfIngresosxml");
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

        finalResult.put("Emitidas", filesXMLFromAWS.get("Recibidas"));
        finalResult.put("Recibidas", filesXMLFromAWS.get("Emitidas"));

        return finalResult;
    }


    public boolean createPolicy(String rfc, String type) {
        try {

            int account_id = accountRepository.getAccountByAccount_id(rfc);
            List<CuentaContable> cuentaContable = new ArrayList<>();
            File folder = new File(server_path + rfc + "/xml/" + type + "/");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    PolicyObjFile policyObjFile = ParserFile.getParse(server_path + rfc + "/xml/" + type + "/" + file.getName());


                    if (policyObjFile.getPolicyObj().getMetodo().equals("P")) {
                        UUID uuid = UUID.randomUUID();
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
                        policyObjFile.setFolio(uuid.toString());

                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getTotalAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);
                            saveObjRepository.save(policytoDB);
                        }
                        //  return true;
                    } else if (policyObjFile.getPolicyObj().getType_of_value().equals("I") && policyObjFile.getPolicyObj().getMetodo().equals("PUE")) {

                        UUID uuid = UUID.randomUUID();
                        List<String> claveProductoServ = getClaveProductoService(policyObjFile.getPolicyObj().getClaveProdServ(), policyObjFile.getPolicyObj().getType_of_value(), policyObjFile.getPolicyObj().getTraslado());
                        cuentaContable.add(cuentaContableRepository.getCuantaContable(policyObjFile.getPolicyObj().getVenta_id()));
                        policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));
                        cuentaContable.add(cuentaContableRepository.getCuantaContable(claveProductoServ.isEmpty() || claveProductoServ == null ? "01" : claveProductoServ.get(0)));
                        policyObjFile.setCuenta_method("216.04");
                        policyObjFile.setDescription_methods(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getCuenta_method()));
                        List<String> id = new ArrayList<>();
                        id.add("216.10");
                        policyObjFile.setTax_id(id);

                        List<String> desc = new ArrayList<>();
                        desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));
                        policyObjFile.setTax_description(desc);
                        List<String> abonos = policyObjFile.getPolicyObj().getRetencion_importe();
                        List<String> cargo = new ArrayList<>();
                        for (String clv : abonos) {
                            cargo.add(cuentaContableRepository.getCuantaContableMethod(clv));
                        }
                        policyObjFile.getPolicyObj().setCargo(cargo);
                        policyObjFile.setFolio(uuid.toString());

                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            //policytoDB.setDebe(String.valueOf(sumDebe));
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getTotalAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            //policytoDB.setHaber(String.valueOf(sumHaber));
                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);

                            saveObjRepository.save(policytoDB);
                        }

//                        return true;
                    } else if (policyObjFile.getPolicyObj().getType_of_value().equals("I") && policyObjFile.getPolicyObj().getMetodo().equals("PPD")) {
                        UUID uuid = UUID.randomUUID();
                        policyObjFile.setCuenta_method("401.07");
                        policyObjFile.setDescription_methods(cuentaContableRepository.getCuantaContableMethod(policyObjFile.getCuenta_method()));
                        List<String> id = new ArrayList<>();
                        id.add("205.99");
                        policyObjFile.setTax_id(id);
                        List<String> desc = new ArrayList<>();
                        desc.add(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getTax_id().get(0)));
                        policyObjFile.setTax_description(desc);
                        policyObjFile.getPolicyObj().setVenta_id("209.01");
                        policyObjFile.getPolicyObj().setVenta_descripcion(cuentaContableRepository.getCuentaContableVenta(policyObjFile.getPolicyObj().getVenta_id()));
                        policyObjFile.setFolio(uuid.toString());


                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {

                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            //policytoDB.setDebe(String.valueOf(sumDebe));
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getTotalAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            // policytoDB.setHaber(String.valueOf(sumHaber));
                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);

                            saveObjRepository.save(policytoDB);

                        }

                        //return true;

                    } else if (policyObjFile.getPolicyObj().getType_of_value().equals("E")) {
                        UUID uuid = UUID.randomUUID();
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
                        policyObjFile.setFolio(uuid.toString());

                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            //policytoDB.setDebe(String.valueOf(sumDebe));
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            //policytoDB.setHaber(String.valueOf(sumHaber));
                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);

                            saveObjRepository.save(policytoDB);
                        }
                        //return true;

                    } else if (policyObjFile.getPolicyObj().getType_of_value().equals("N")) {
                        UUID uuid = UUID.randomUUID();
                        List<String> claveProductoServ = getClaveProductoService(policyObjFile.getPolicyObj().getClaveProdServ(), policyObjFile.getPolicyObj().getType_of_value(), policyObjFile.getPolicyObj().getTraslado());
                        List<String> accounts = getCuentaCobtableList(claveProductoServ);

                        //method payment (Abono)
                        policyObjFile.setCuenta_method(methodOfPaymentRepository.getCuentaContable(policyObjFile.getTypeOfPayment()));
                        policyObjFile.setDescription_methods(cuentaContableRepository.getCuantaContableMethod(policyObjFile.getCuenta_method()));

                        claveProductoServ.add(policyObjFile.getCuenta_method());
                        accounts.add(policyObjFile.getDescription_methods());
                        policyObjFile.setTax_id(claveProductoServ);
                        policyObjFile.setTax_description(accounts);
                        policyObjFile.setFolio(uuid.toString());

                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            //policytoDB.setDebe(String.valueOf(sumDebe));
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            //policytoDB.setHaber(String.valueOf(sumHaber));
                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);

                            saveObjRepository.save(policytoDB);

                        }
                        //return true;

                    } else {
                        // (Cargo)
                        UUID uuid = UUID.randomUUID();
                        policyObjFile.setTax_id(getIvaIeps(policyObjFile.getPolicyObj().getIva(), policyObjFile.getPolicyObj().getType_of_value(), policyObjFile.getPolicyObj().getAmount()));
                        policyObjFile.setTax_description(getCuentaCobtableList(policyObjFile.getTax_id()));
                        policyObjFile.setFolio(uuid.toString());
                        if (CreateFilePDFPolicy.makeFile(policyObjFile, file.getName().replace(".xml", ""), rfc, type)) {
                            PolicytoDB policytoDB = new PolicytoDB();
                            policytoDB.setCliente(policyObjFile.getClient());
                            policytoDB.setUsuario(rfc);
                            policytoDB.setTipo(type);
                            policytoDB.setProveedor(policyObjFile.getCompanyName());
                            policytoDB.setPoliza(policyObjFile.getFolio());
                            policytoDB.setCuenta(Double.parseDouble(policyObjFile.getCuenta_method()));
                            policytoDB.setDescripcion(policyObjFile.getDescription_methods());

                            List<String> debe = policyObjFile.getPolicyObj().getTraslado();
                            int sumDebe = 0;
                            if (debe != null) {
                                for (String db : debe) {
                                    sumDebe += Integer.parseInt(db);

                                }
                            }
                            policytoDB.setDebe(policyObjFile.getPolicyObj().getAmount());
                            List<String> haber = policyObjFile.getPolicyObj().getRetencion_importe();
                            int sumHaber = 0;
                            if (haber != null) {
                                for (String db : haber) {
                                    sumHaber += Integer.parseInt(db);

                                }
                            }


                            // policytoDB.setHaber(String.valueOf(sumHaber));
                            policytoDB.setHaber(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()));
                            policytoDB.setFecha(policyObjFile.getDate());
                            policytoDB.setReferencia(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID());
                            policytoDB.setTotal(policyObjFile.getPolicyObj().getTotalAmount());
                            policytoDB.setSaldo_inicial("0");
                            double fin = Double.parseDouble(policytoDB.getSaldo_inicial()) + Double.parseDouble(policytoDB.getTotal());
                            policytoDB.setSaldo_final(String.valueOf(fin));
                            policytoDB.setAccount_id(account_id);

                            saveObjRepository.save(policytoDB);
                        }
                        //return true;

                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" e:   " + e.getMessage());
        }
        return false;
    }

}


