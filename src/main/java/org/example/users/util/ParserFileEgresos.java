package org.example.users.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.users.model.PolicyObjFile;
import org.example.users.model.PolicyObjParser;
import org.example.users.model.TypeOfEgresoN;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class ParserFileEgresos {
    public static Logger LOGGER = LogManager.getLogger(PolicyObjParser.class);
    private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all/";
    private static final String server_path = "/home/ubuntu/endpoints/eTribute-all/";

    public static PolicyObjFile getParse(String path) {

        PolicyObjParser values = new PolicyObjParser();

        try {


            File archivoXML = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            Element comprobanteElement = doc.getDocumentElement();


            //LOGGER.info(" type {}", path.substring(64, 71));
            Element total = doc.getDocumentElement();
            //"------------- total  ---------------
            values.setTotalAmount(total.getAttribute("Total"));

            //LOGGER.info("total Amount {}", values.getTotalAmount());

            Element date = doc.getDocumentElement();
            //System.out.println(date.getAttribute("Fecha"));
            String currentDate = date.getAttribute("Fecha");
            //LOGGER.info("currentDate {}", currentDate);

            Element payment1 = doc.getDocumentElement();
            values.setTypeOfPayment(payment1.getAttribute("FormaPago").isEmpty() || payment1.getAttribute("FormaPago") == null ? "99" : payment1.getAttribute("FormaPago"));


            Element method2 = doc.getDocumentElement();
            values.setMethodPayment(method2.getAttribute("MetodoPago"));
            values.setMetodo(method2.getAttribute("MetodoPago"));
            NodeList repectEgr2 = doc.getElementsByTagName("cfdi:Receptor");
            Element rEgr2 = (Element) repectEgr2.item(0);
            values.setClient(rEgr2.getAttribute("Nombre"));

            NodeList rfc = doc.getElementsByTagName("cfdi:Emisor");
            Element RFC = (Element) rfc.item(0);
            values.setRfc(RFC.getAttribute("Rfc"));

            NodeList emisor2 = doc.getElementsByTagName("cfdi:Emisor");
            Element rcp2 = (Element) emisor2.item(0);
            values.setCompanyName(rcp2.getAttribute("Nombre"));

            Element comprobante = doc.getDocumentElement();


            values.setTypeOfComprobante(comprobante.getAttribute("TipoDeComprobante"));

            if (values.getTypeOfComprobante().equals("P")) {
                NodeList py = comprobanteElement.getElementsByTagName("pago20:Pago");
                Element rEgr = (Element) py.item(0);
                values.setMethodPayment(rEgr.getAttribute("FormaDePagoP"));
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            } else {
                Element method = doc.getDocumentElement();
                String methodPayment = method.getAttribute("MetodoPago");
                values.setMethodPayment(methodPayment);
            }
            //LOGGER.info("MethodPayment {}", values.getMethodPayment());
            NodeList timbre = doc.getElementsByTagName("tfd:TimbreFiscalDigital");
            Element uudi = (Element) timbre.item(0);
            values.setTimbreFiscalDigital_UUID(uudi.getAttribute("UUID"));
            NodeList conceptosList = doc.getElementsByTagName("cfdi:Concepto");
            Element description = (Element) conceptosList.item(0);
            values.setConcepto_Descripcion(description.getAttribute("Descripcion"));
            // LOGGER.info(" concepto {} ", values.getConcepto_Descripcion());
            //LOGGER.info("{} ", values);
            // System.out.println();


            String methodPayment;
            if (values.getTypeOfComprobante().equals("P")) {
                NodeList py = comprobanteElement.getElementsByTagName("pago20:Pago");
                Element rEgr = (Element) py.item(0);
                values.setMethodPayment(rEgr.getAttribute("FormaDePagoP"));
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            } else {
                Element method = doc.getDocumentElement();
                methodPayment = method.getAttribute("MetodoPago");
                values.setMethodPayment(methodPayment);
            }
            Element method = doc.getDocumentElement();

            values.setMetodo(method.getAttribute("MetodoPago"));

            NodeList repectEgr = comprobanteElement.getElementsByTagName("cfdi:Receptor");
            Element rEgr = (Element) repectEgr.item(0);

            NodeList datoCli = comprobanteElement.getElementsByTagName("cfdi:Receptor");
            Element cliente = (Element) datoCli.item(0);
            //System.out.println(" cliente --- > "+cliente.getAttribute("Nombre"));
            String cli = cliente.getAttribute("Nombre");
            String typeOf = null;
            if (!values.getTypeOfComprobante().equals("P")) {

                if (!values.getTypeOfComprobante().isEmpty()) {
                    //System.out.println(methodPayment + rEgr.getAttribute("Nombre") +cli);
                    if (values.getMethodPayment().equals("PUE") && rEgr.getAttribute("Nombre").equals(cli)) {
                        typeOf = "Egreso";
                        //System.out.println(" ---> Egreso");
                    } else if (values.getMethodPayment().equals("PPD") && rEgr.getAttribute("Nombre").equals(cli)) {
                        typeOf = "Diario";
                        //System.out.println("---> Diario");
                    }
                }
            }

            if (comprobante.getAttribute("TipoDeComprobante").equals("I")) {

                NodeList impe = comprobanteElement.getElementsByTagName("cfdi:Impuestos");
                Element ime = (Element) impe.item(impe.getLength() - 1);
                values.setImpuestos(ime.getAttribute("TotalImpuestosTrasladados").isEmpty() ? "0" : ime.getAttribute("TotalImpuestosTrasladados"));

                NodeList rgm = comprobanteElement.getElementsByTagName("cfdi:Emisor");
                Element rgim = (Element) rgm.item(0);
                String regimen = rgim.getAttribute("RegimenFiscal");
                values.setRegimen(regimen);
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            }

            //---------------
            NodeList impe1 = comprobanteElement.getElementsByTagName("cfdi:Impuestos");
            NodeList impe2 = comprobanteElement.getElementsByTagName("pago20:ImpuestosP");

            String totalImpuestosTrasladados = "0";

            if (impe1.getLength() > 0) {
                Element ime1 = (Element) impe1.item(0);
                totalImpuestosTrasladados = ime1.getAttribute("TotalImpuestosTrasladados");
                if (!totalImpuestosTrasladados.isEmpty() && !totalImpuestosTrasladados.equals("0")) {
                    values.setImpuestos(totalImpuestosTrasladados);
                    //LOGGER.info("Valor encontrado en cfdi:Impuestos: {}", totalImpuestosTrasladados);
                }
            }

            if (impe2.getLength() > 0) {
                Element ime2 = (Element) impe2.item(0);
                totalImpuestosTrasladados = ime2.getAttribute("TotalImpuestosTrasladados");
                if (!totalImpuestosTrasladados.isEmpty() && !totalImpuestosTrasladados.equals("0")) {
                    values.setImpuestos(totalImpuestosTrasladados);
                    // LOGGER.info("Valor encontrado en pago20:ImpuestosP: {}", totalImpuestosTrasladados);
                }
            }

            if (values.getTypeOfComprobante().equals("E")) {
                if (values.getRetencion_importe() == null) {
                    values.setRetencion_importe(new ArrayList<>());
                    values.setCargo(new ArrayList<>());
                    values.setAbono(new ArrayList<>());
                    values.setTax_amount(new ArrayList<>());
                    values.setIva(new HashMap<>());
                    values.setImpuestos("");
                    values.setVenta_id("");
                    values.setVenta_descripcion("");
                    values.setClaveProdServ(new ArrayList<>());
                }

            }

            if (values.getTypeOfComprobante().equals("N")) {
                values.setAmount("0");
                NodeList receptor = doc.getElementsByTagName("nomina12:Deducciones");
                Element receptor1 = (Element) receptor.item(0);
                TypeOfEgresoN tipoDeEgresoN = new TypeOfEgresoN();
                tipoDeEgresoN.setCuenta("601.23");
                tipoDeEgresoN.setDescripcion("Previsión social");
                tipoDeEgresoN.setAbonoIMSS("211.01");
                tipoDeEgresoN.setAbonoDescipcion("Provisión de IMSS patronal por pagar");
                tipoDeEgresoN.setIsr("216.01");
                tipoDeEgresoN.setIsrDescripcion("Impuestos retenidos de ISR por sueldos y salarios");
                List<String> percepciones = new ArrayList<>();
                percepciones.add(receptor1.getAttribute("TotalOtrasDeducciones") == null ? "0" : receptor1.getAttribute("TotalOtrasDeducciones"));
                percepciones.add(receptor1.getAttribute("TotalImpuestosRetenidos") == null ? "0" : receptor1.getAttribute("TotalImpuestosRetenidos"));
                tipoDeEgresoN.setPercepciones(percepciones);
                values.setTypeOfEgresoN(tipoDeEgresoN);

                values.setRetencion_importe(new ArrayList<>());
                values.setImpuestos("0.0");
                values.setCargo(new ArrayList<>());
                values.setAbono(new ArrayList<>());
                values.setVenta_id("209.01");
                values.setIva(new HashMap<>());
                values.setTax_amount(new ArrayList<>());

            }


            NodeList impeT = comprobanteElement.getElementsByTagName("cfdi:Traslados");
            //Element tras= (Element) impeT.item(0);
            //LOGGER.info("impeT: {} ", impeT.getLength());


            List<String> transladoIm = new ArrayList<>();
            for (int i = 0; i < impeT.getLength(); i++) {
                Element retencionR = (Element) impeT.item(i);
                //System.out.println(retencionR.getAttribute("Impuesto"));
                transladoIm.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

            }


            //"------------- Nombre de empresa ----------------
            NodeList emisor = comprobanteElement.getElementsByTagName("cfdi:Emisor");
            Element rcp = (Element) emisor.item(0);
            String companyName = rcp.getAttribute("Nombre");

            //"------------- RegimenFiscalReceptor ----------------
            NodeList receptor = comprobanteElement.getElementsByTagName("cfdi:Receptor");
            Element regimen = (Element) receptor.item(0);
            values.setRegimen(regimen.getAttribute("RegimenFiscalReceptor"));


            //"------------- UsoCFDI ----------------
            Element useCFDI = (Element) receptor.item(0);
            values.setUsoCFDI(useCFDI.getAttribute("UsoCFDI"));


            //"------------- Concepto ----------------
            conceptosList = comprobanteElement.getElementsByTagName("cfdi:Concepto");
            description = (Element) conceptosList.item(0);
            values.setConcepto_Descripcion(description.getAttribute("Descripcion"));

            //"------------- amount ----------------
            Element amount = (Element) conceptosList.item(0);
            values.setAmount(amount.getAttribute("Importe"));


            //"------------- ClaveProdServ ----------------
            // para tipo P no es necesario la ClaveProdServ

            if (!values.getTypeOfComprobante().equals("P")) {
                NodeList ClaveProdServ = comprobanteElement.getElementsByTagName("cfdi:Concepto");
                Element claveProdServ = (Element) ClaveProdServ.item(0);
                if (claveProdServ.getAttribute("ClaveProdServ") != null) {

                    values.setClaveProdServ(Collections.singletonList(claveProdServ.getAttribute("ClaveProdServ")));
                } else {
                    values.setClaveProdServ(new ArrayList<>());
                }
                //------------- Traslado ----------------
                NodeList traslados = comprobanteElement.getElementsByTagName("cfdi:Traslado");
                //LOGGER.info("traslados: {} ", traslados.getLength());
                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

                }

                values.setTraslado(translado);

                //"------------- Retencion ----------------
                NodeList retencion = comprobanteElement.getElementsByTagName("cfdi:Concepto");
                List<String> retencion_importe = new ArrayList<>();
                //LOGGER.info("retencion: {} ", retencion.getLength());

                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe"));
                }
            } else {
                values.setTraslado(new ArrayList<>());
                values.setRetencion_importe(new ArrayList<>());
                values.setClaveProdServ(new ArrayList<>());
            }


            //"------------- TimbreFiscalDigital ----------------
            timbre = comprobanteElement.getElementsByTagName("tfd:TimbreFiscalDigital");
            uudi = (Element) timbre.item(0);
            values.setTimbreFiscalDigital_UUID(uudi.getAttribute("UUID"));
            //System.out.println(typeOfCom + " --- " + methodPayment);
            if (!values.getTypeOfComprobante().equals("I") && !values.getMethodPayment().equals("PPD")) {
                NodeList imp = comprobanteElement.getElementsByTagName("pago20:Totales");
                if (imp.getLength() > 0) {
                    Element ip = (Element) imp.item(0);
                    values.setImpuestos(ip.getAttribute("MontoTotalPagos"));
                }
                NodeList dr = comprobanteElement.getElementsByTagName("pago20:TrasladoDR");
                Element d = (Element) dr.item(0);
                if (d == null) {
                    LOGGER.error(" archivo no cumpple caracteristicas de R´s:  {}", path.substring(server_path.length() + values.getRfc().length() + values.getTypeOfPayment().length() + "xml/".length(), path.length()));
                } else {
                    values.setAmount(d.getAttribute("ImporteDR"));
                }
            }

            return new PolicyObjFile(values, path, companyName, cli, currentDate.substring(0, 10), typeOf, values.getTypeOfPayment());

        } catch (Exception e) {
            LOGGER.error("ParserFileEgresos " + e.getMessage() + e.getLocalizedMessage() + e.getCause());
        }
        return null;
    }

}
