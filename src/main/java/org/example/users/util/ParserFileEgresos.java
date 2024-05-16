package org.example.users.util;

import org.example.users.model.PolicyObjFile;
import org.example.users.model.PolicyObjParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class ParserFileEgresos {

    public static PolicyObjFile getParse(String path) {

        PolicyObjParser values = new PolicyObjParser();

        try {

            File archivoXML = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);
            doc.getDocumentElement().normalize();
            Element comprobanteElement = doc.getDocumentElement();


            Element total = doc.getDocumentElement();
            //"------------- total  ---------------
            values.setTotalAmount(total.getAttribute("Total"));

            Element date = doc.getDocumentElement();
            //System.out.println(date.getAttribute("Fecha"));
            String currentDate = date.getAttribute("Fecha");

            //System.out.println(currentDate.substring(0,10));
            //System.out.println("newdate: "+currentDate.replace("T"," "));


            Element payment = doc.getDocumentElement();
            //System.out.println("payment --- "+payment.getAttribute("FormaPago"));
            Element comprobante = doc.getDocumentElement();
            //System.out.println("comprobante --- "+comprobante.getAttribute("TipoDeComprobante"));
            String methodPayment = null;
            String typeOfCom = comprobante.getAttribute("TipoDeComprobante");
            if (typeOfCom.equals("P")) {
                NodeList py = comprobanteElement.getElementsByTagName("pago20:Pago");
                Element rEgr = (Element) py.item(0);
                values.setMethodPayment(rEgr.getAttribute("FormaDePagoP"));

            } else {
                Element method = doc.getDocumentElement();
                methodPayment = method.getAttribute("MetodoPago");
                values.setMethodPayment(methodPayment);
            }


            NodeList repectEgr = comprobanteElement.getElementsByTagName("cfdi:Receptor");
            Element rEgr = (Element) repectEgr.item(0);
            //System.out.println(" rEgr --- > "+rEgr.getAttribute("Nombre"));


            NodeList datoCli = comprobanteElement.getElementsByTagName("cfdi:Receptor");
            Element cliente = (Element) datoCli.item(0);
            //System.out.println(" cliente --- > "+cliente.getAttribute("Nombre"));
            String cli = cliente.getAttribute("Nombre");
            String typeOf = null;
            if (!typeOfCom.equals("P")) {

                if (!methodPayment.isEmpty()) {
                    //System.out.println(methodPayment + rEgr.getAttribute("Nombre") +cli);
                    if (methodPayment.equals("PUE") && rEgr.getAttribute("Nombre").equals(cli)) {
                        typeOf = "Egreso";
                        //System.out.println(" ---> Egreso");
                    } else if (methodPayment.equals("PPD") && rEgr.getAttribute("Nombre").equals(cli)) {
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


            }

            //---------------

//            NodeList impe = comprobanteElement.getElementsByTagName("cfdi:Impuestos");
//            Element ime = (Element) impe.item(impe.getLength() - 1);
//            values.setImpuestos(ime.getAttribute("TotalImpuestosTrasladados").isEmpty() ? "0" : ime.getAttribute("TotalImpuestosTrasladados"));


//            NodeList impeT = comprobanteElement.getElementsByTagName("cfdi:Traslados");
//            //Element tras= (Element) impeT.item(0);
//
//
//            List<String> transladoIm = new ArrayList<>();
//            for (int i = 0; i < impeT.getLength(); i++) {
//                Element retencionR = (Element) impeT.item(i);
//                System.out.println(retencionR.getAttribute("Impuesto"));
//                //translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));
//
//            }

            //System.out.println("Impuesto--  "+tras.getAttribute("Impuesto"));


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
            NodeList conceptosList = comprobanteElement.getElementsByTagName("cfdi:Concepto");
            Element description = (Element) conceptosList.item(0);
            values.setConcepto_Descripcion(description.getAttribute("Descripcion"));

            //"------------- amount ----------------
            Element amount = (Element) conceptosList.item(0);
            values.setAmount(amount.getAttribute("Importe"));


            //"------------- ClaveProdServ ----------------
            // para tipo P no es necesario la ClaveProdServ

            if (!typeOfCom.equals("P")) {
                NodeList ClaveProdServ = comprobanteElement.getElementsByTagName("cfdi:Concepto");
                Element claveProdServ = (Element) ClaveProdServ.item(0);
                values.setClaveProdServ(Collections.singletonList(claveProdServ.getAttribute("ClaveProdServ")));

                //------------- Traslado ----------------
                NodeList traslados = comprobanteElement.getElementsByTagName("cfdi:Traslado");
                List<String> translado = new ArrayList<>();
                for (int i = 0; i < traslados.getLength(); i++) {
                    Element retencionR = (Element) traslados.item(i);
                    translado.add(retencionR.getAttribute("Importe").isEmpty() ? "0" : retencionR.getAttribute("Importe"));

                }

                values.setTraslado(translado);

                //"------------- Retencion ----------------
                NodeList retencion = comprobanteElement.getElementsByTagName("cfdi:Concepto");
                List<String> retencion_importe = new ArrayList<>();

                for (int i = 0; i < retencion.getLength(); i++) {
                    Element retencionR = (Element) retencion.item(i);
                    retencion_importe.add(retencionR.getAttribute("Importe"));
                }
            } else {
                values.setTraslado(new ArrayList<>());
                values.setRetencion_importe(new ArrayList<>());
            }


            //"------------- TimbreFiscalDigital ----------------
            NodeList timbre = comprobanteElement.getElementsByTagName("tfd:TimbreFiscalDigital");
            Element uudi = (Element) timbre.item(0);
            values.setTimbreFiscalDigital_UUID(uudi.getAttribute("UUID"));
            //System.out.println(typeOfCom + " --- " + methodPayment);
            if (!typeOfCom.equals("I") && !methodPayment.equals("PPD")) {
                NodeList imp = comprobanteElement.getElementsByTagName("pago20:Totales");
                Element ip = (Element) imp.item(0);
                values.setImpuestos(ip.getAttribute("MontoTotalPagos"));
                NodeList dr = comprobanteElement.getElementsByTagName("pago20:TrasladoDR");
                Element d = (Element) dr.item(0);
                values.setAmount(d.getAttribute("ImporteDR"));
            }


        } catch (Exception e) {
            System.out.println("ParserFileEgresos " + e.getMessage() + e.getLocalizedMessage());
        }
        return null;
    }

}
