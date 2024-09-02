package org.example.users.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.users.model.Descripcion;
import org.example.users.model.PolicyObjFile;
import org.example.users.model.PolicyObjParser;
import org.example.users.model.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;


public class ParserFileIngresos {

    public static final Logger LOGGER = LogManager.getLogger(ParserFileIngresos.class);
    //private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all/";
    private static final String server_path = "/home/ubuntu/endpoints/eTribute-all/";

    public static Response getParseValues(String pathFromAWS, String rfc, String type, String fileName) {

        Response response = new Response();
        Descripcion descripcion = new Descripcion();

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(createFileFromURL(pathFromAWS, rfc, type, fileName));
            doc.getDocumentElement().normalize();
            Element comprobanteElement = doc.getDocumentElement();

            /* get date from xml*/
            Element date = doc.getDocumentElement();
            descripcion.setFecha(date.getAttribute("Fecha").substring(0, 10));

            if (type.equals("EGRESOS")) {
                NodeList repectEgr = comprobanteElement.getElementsByTagName("cfdi:Emisor");
                Element cliente = (Element) repectEgr.item(0);
                descripcion.setCliente(cliente.getAttribute("Nombre"));
            } else if (type.equals("INGRESOS")) {
                NodeList repectEgr = comprobanteElement.getElementsByTagName("cfdi:Receptor");
                Element cliente = (Element) repectEgr.item(0);
                descripcion.setCliente(cliente.getAttribute("Nombre"));
            }

            Element total = doc.getDocumentElement();
            descripcion.setCantidad(total.getAttribute("Total"));

            response.setDescripcion(descripcion);
            response.setUrl_xml(pathFromAWS);
            //archivoXML.delete();


        } catch (Exception e) {
            LOGGER.error("error { " + e.getLocalizedMessage() + " }");
        }
        return response;
    }


    public static PolicyObjFile getParse(String path) {
        PolicyObjParser values = new PolicyObjParser();

        try {

            File archivoXML = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();

            Element total = doc.getDocumentElement();
            //"------------- total  ---------------
            values.setTotalAmount(total.getAttribute("Total"));

            Element date = doc.getDocumentElement();
            String currentDate = date.getAttribute("Fecha");
            Element payment1 = doc.getDocumentElement();
            values.setTypeOfPayment(payment1.getAttribute("FormaPago").isEmpty() || payment1.getAttribute("FormaPago") == null ? "99" : payment1.getAttribute("FormaPago"));

            Element comprobante = doc.getDocumentElement();
            values.setTypeOfComprobante(comprobante.getAttribute("TipoDeComprobante"));
            Element method = doc.getDocumentElement();
            values.setMethodPayment(method.getAttribute("MetodoPago"));
            values.setMetodo(method.getAttribute("MetodoPago"));

            NodeList repectEgr = doc.getElementsByTagName("cfdi:Receptor");
            Element rEgr = (Element) repectEgr.item(0);
            values.setClient(rEgr.getAttribute("Nombre"));
            NodeList rfc = doc.getElementsByTagName("cfdi:Emisor");
            Element RFC = (Element) rfc.item(0);
            values.setRfc(RFC.getAttribute("Rfc"));

            String typeOf = "";
            if (values.getMethodPayment().equals("PUE")) {
                typeOf = "Egreso";
            } else if (values.getMethodPayment().equals("PPD")) {
                typeOf = "Diario";
            }

            NodeList emisor = doc.getElementsByTagName("cfdi:Emisor");
            Element rcp = (Element) emisor.item(0);
            values.setCompanyName(rcp.getAttribute("Nombre"));
            NodeList receptor = doc.getElementsByTagName("cfdi:Receptor");
            Element regimen = (Element) receptor.item(0);
            values.setRegimen(regimen.getAttribute("RegimenFiscalReceptor"));
            Element useCFDI = (Element) receptor.item(0);
            values.setUsoCFDI(useCFDI.getAttribute("UsoCFDI"));

            NodeList conceptosList = doc.getElementsByTagName("cfdi:Concepto");
            Element description = (Element) conceptosList.item(0);
            values.setConcepto_Descripcion(description.getAttribute("Descripcion"));

            Element amount = (Element) conceptosList.item(0);
            values.setAmount(amount.getAttribute("Importe"));

            NodeList timbre = doc.getElementsByTagName("tfd:TimbreFiscalDigital");
            Element uudi = (Element) timbre.item(0);
            values.setTimbreFiscalDigital_UUID(uudi.getAttribute("UUID"));
            if (values.getTypeOfComprobante().equals("I") && values.getMethodPayment().equals("PUE")) {

                NodeList impe = doc.getElementsByTagName("cfdi:Impuestos");

                Element ime = (Element) impe.item(0);

                values.setImpuestos(ime.getAttribute("Traslados").isEmpty() ? "0" : ime.getAttribute("Traslados"));

                NodeList ClaveProdServ = doc.getElementsByTagName("cfdi:Concepto");
                Element claveProdServ = (Element) ClaveProdServ.item(0);
                List<String> clv = new ArrayList<>();
                clv.add(claveProdServ.getAttribute("ClaveProdServ"));
                values.setClaveProdServ(clv);

                NodeList traslados = doc.getElementsByTagName("cfdi:Traslado");
                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

                }
                values.setTraslado(translado);

                NodeList retencion = doc.getElementsByTagName("cfdi:Retencion");
                List<String> retencion_importe = new ArrayList<>();
                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));
                }
                values.setRetencion_importe(retencion_importe);
                values.setVenta_id("102.01");
                List<String> abono = new ArrayList<>();
                abono.add("401.01");
                abono.add("208.01");
                values.setAbono(abono);

                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            } else if (values.getTypeOfComprobante().equals("I") && values.getMethodPayment().equals("PPD")) {

                NodeList retencion = doc.getElementsByTagName("cfdi:Retencion");
                List<String> retencion_importe = new ArrayList<>();
                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));
                }
                values.setRetencion_importe(retencion_importe);
                NodeList traslados = doc.getElementsByTagName("cfdi:Traslado");

                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

                }
                values.setTraslado(translado);
                values.setSubtotal(Double.parseDouble(comprobante.getAttribute("SubTotal")));

                //si iva es 002 setear a 006 siempre y cuando sea Retencion
                NodeList iva = doc.getElementsByTagName("cfdi:Retencion");
                Map<String, String> iva2 = new HashMap<>();
                for (int j = 0; j < iva.getLength(); j++) {
                    Element clv = (Element) iva.item(j);
                    iva2.put(clv.getAttribute("Impuesto"), clv.getAttribute("Importe"));
                }
                values.setIva(iva2);

                NodeList tipo = doc.getElementsByTagName("cfdi:Concepto");
                List<String> claveProd = new ArrayList<>();
                for (int j = 0; j < tipo.getLength(); j++) {
                    Element clv = (Element) tipo.item(j);
                    claveProd.add(clv.getAttribute("ClaveProdServ"));
                }
                values.setClaveProdServ(claveProd);
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            } else if (values.getTypeOfComprobante().equals("E")) {
                NodeList rcp2 = doc.getElementsByTagName("cfdi:Receptor");
                Element rcp3 = (Element) rcp2.item(0);
                values.setMetodo(rcp3.getAttribute("Nombre"));

                values.setSubtotal(Double.parseDouble(comprobante.getAttribute("SubTotal")));

                NodeList impe = doc.getElementsByTagName("cfdi:Impuestos");
                Element ime = (Element) impe.item(impe.getLength() - 1);
                values.setImpuestos(ime.getAttribute("TotalImpuestosTrasladados").isEmpty() ? "0" : ime.getAttribute("TotalImpuestosTrasladados"));

                NodeList ClaveProdServ = doc.getElementsByTagName("cfdi:Concepto");
                Element claveProdServ = (Element) ClaveProdServ.item(0);
                List<String> clv = new ArrayList<>();
                clv.add(claveProdServ.getAttribute("ClaveProdServ"));
                values.setClaveProdServ(clv);

                NodeList traslados = doc.getElementsByTagName("cfdi:Traslado");
                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

                }
                values.setTraslado(translado);

                NodeList retencion = doc.getElementsByTagName("cfdi:Concepto");
                List<String> retencion_importe = new ArrayList<>();
                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe"));
                }
                values.setRetencion_importe(retencion_importe);
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            } else if (values.getTypeOfComprobante().equals("N")) {
                values.setSubtotal(Double.parseDouble(comprobante.getAttribute("SubTotal")));
                NodeList impe = doc.getElementsByTagName("cfdi:Impuestos");

                if (impe.getLength() > 0) {
                    Element impuestosElement = (Element) impe.item(0);
                    String totalImpuestosTrasladados = impuestosElement.getAttribute("TotalImpuestosTrasladados");

                    if (totalImpuestosTrasladados.isEmpty()) {
                        totalImpuestosTrasladados = "0";
                    }
                    values.setImpuestos(totalImpuestosTrasladados);
                } else {
                    values.setImpuestos("0");
                }
                NodeList imp = doc.getElementsByTagName("cfdi:Traslado");
                Map<String, String> iva = new HashMap<>();
                List<String> importte = new ArrayList<>();
                for (int j = 0; j < imp.getLength(); j++) {
                    Element clv = (Element) imp.item(j);
                    iva.put(clv.getAttribute("Impuesto"), clv.getAttribute("Importe"));
                }
                values.setIva(iva);
                importte.addAll(iva.values());

                values.setTax_amount(importte);
                NodeList tipo = doc.getElementsByTagName("cfdi:Concepto");
                List<String> claveProd = new ArrayList<>();
                for (int j = 0; j < tipo.getLength(); j++) {
                    Element clv = (Element) tipo.item(j);
                    claveProd.add(clv.getAttribute("ClaveProdServ"));
                }
                values.setClaveProdServ(claveProd);

                NodeList traslados = doc.getElementsByTagName("nomina12:Deduccion");
                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Clave").isEmpty() ? "N/A" : retencionR.getAttribute("Clave"));

                }
                values.setTraslado(translado);

                NodeList retencion = doc.getElementsByTagName("nomina12:Deduccion");
                List<String> retencion_importe = new ArrayList<>();
                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe"));
                }

                NodeList percep = doc.getElementsByTagName("nomina12:Percepciones");
                Element totalSueldos = (Element) percep.item(0);
                String totaS = totalSueldos.getAttribute("TotalSueldos") == null ? "0" : totalSueldos.getAttribute("TotalSueldos");
                retencion_importe.add(totaS.isEmpty() || totaS == null ? "0" : totaS);
                retencion_importe.add(values.getTotalAmount());
                values.setRetencion_importe(retencion_importe);
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());


            } else if (values.getTypeOfComprobante().equals("P")) {
                NodeList percep = doc.getElementsByTagName("pago20:Totales");
                Element totalSueldos = (Element) percep.item(0);
                String dou = totalSueldos.getAttribute("TotalTrasladosImpuestoIVA16").isEmpty() || totalSueldos.getAttribute("TotalTrasladosImpuestoIVA16") == null ? "0" : totalSueldos.getAttribute("TotalTrasladosImpuestoIVA16");
                values.setSubtotal(Double.parseDouble(dou.isEmpty() || dou == null ? "0" : dou));

                NodeList pago = doc.getElementsByTagName("pago20:Pago");
                Element pay = (Element) pago.item(0);
                values.setImpuestos(pay.getAttribute("FormaDePagoP"));
                values.setUsoCFDI(pay.getAttribute("FechaPago").substring(0, 10));
                values.setTypeOfComprobante(pay.getAttribute("TipoCambioP"));
                values.setAmount(pay.getAttribute("Monto"));

                NodeList docu = doc.getElementsByTagName("pago20:DoctoRelacionado");
                Element d = (Element) docu.item(0);
                values.setTimbreFiscalDigital_UUID(d.getAttribute("IdDocumento") + " " + d.getAttribute("Folio"));
                values.setConcepto_Descripcion(d.getAttribute("MonedaDR"));
                values.setTotalAmount(d.getAttribute("ImpSaldoAnt"));
                List<String> clv = new ArrayList<>();
                clv.add(d.getAttribute("ImpPagado"));
                values.setClaveProdServ(clv);
                values.setRegimen(d.getAttribute("NumParcialidad"));
                values.setImpuestos(values.getImpuestos() == null ? "0" : values.getImpuestos());
                values.setVenta_id(values.getVenta_id() == null ? "defaultVentaId" : values.getVenta_id());
                values.setVenta_descripcion(values.getVenta_descripcion() == null ? "defaultVentaDescripcion" : values.getVenta_descripcion());
                values.setCargo(values.getCargo() == null ? new ArrayList<>() : values.getCargo());
                values.setAbono(values.getAbono() == null ? new ArrayList<>() : values.getAbono());
                values.setTax_amount(values.getTax_amount() == null ? new ArrayList<>() : values.getTax_amount());

            }

            return new PolicyObjFile(values, path, values.getCompanyName(), values.getClient(), currentDate.substring(0, 10), typeOf, values.getTypeOfComprobante());

        } catch (Exception e) {
            System.out.println("ParserFileIngresos " + e.getMessage() + e.getLocalizedMessage());
        }
        return null;
    }


    public static File createFileFromURL(String urlFromAWS, String rfc, String type, String fileName) {
        try {

            URL url = new URL(urlFromAWS);
            File file = new File(server_path + rfc + "/xml/" + type + "/" + fileName);
            FileUtils.copyURLToFile(url, file);
            return file;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
