package org.example.users.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.users.model.PolicyObjFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.users.util.CFDI.*;
import static org.example.users.util.Regimen.*;


public class CreateFilePDFPolicy {

    //private static final String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all/";
    private static final String server_path = "/home/ubuntu/endpoints/eTribute-all/";
    public static final Logger LOGGER = LogManager.getLogger(CreateFilePDFPolicy.class);

    public CreateFilePDFPolicy() {
    }


    public static boolean makeFileEgreso(PolicyObjFile policyObjFile, String fileName, String rfc, String type) {
        try {

            String cargoTotal = "";
            String impuesto= "";
            File uploadDir = new File(server_path + rfc + "/pdf/" + type + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(server_path + rfc + "/pdf/" + type + "/" + fileName + ".pdf"));
            document.open();

            //---------------------   header of file  --------------------- //

            PdfPTable table = new PdfPTable(2);

            PdfPCell client = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            client.setHorizontalAlignment(Element.ALIGN_CENTER);
            client.setBorderColor(BaseColor.WHITE);
            client.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell folio = new PdfPCell(new Paragraph("P ó l i z a" + "\n" + " Tipo: " + policyObjFile.getTypeOf() + "  Folio: " + policyObjFile.getFolio(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK.darker())));
            folio.setHorizontalAlignment(Element.ALIGN_CENTER);
            folio.setBorderColor(BaseColor.WHITE);
            folio.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell company = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            company.setHorizontalAlignment(Element.ALIGN_CENTER);
            company.setBorderColor(BaseColor.WHITE);
            company.setBackgroundColor(new BaseColor(229, 231, 233));


            PdfPCell date_ = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
            date_.setHorizontalAlignment(Element.ALIGN_CENTER);
            date_.setBorderColor(BaseColor.WHITE);
            date_.setBackgroundColor(new BaseColor(229, 231, 233));


            table.addCell(client);
            table.addCell(folio);
            table.addCell(company);
            table.addCell(date_);
            table.setHorizontalAlignment(2);
            table.setWidthPercentage(60);


            PdfPTable headerTable = new PdfPTable(4);
            headerTable.setWidthPercentage(100);

            PdfPCell account = new PdfPCell(new Paragraph("Cuenta", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            account.setHorizontalAlignment(Element.ALIGN_CENTER);
            account.setBorderColor(BaseColor.WHITE);
            account.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell concept = new PdfPCell(new Paragraph("Concepto", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            concept.setHorizontalAlignment(Element.ALIGN_CENTER);
            concept.setPaddingRight(40);
            concept.setBorderColor(BaseColor.WHITE);
            concept.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell charge = new PdfPCell(new Paragraph("Cargo", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            charge.setHorizontalAlignment(Element.ALIGN_CENTER);
            charge.setBorderColor(BaseColor.WHITE);
            charge.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell payment = new PdfPCell(new Paragraph("Abono", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            payment.setHorizontalAlignment(Element.ALIGN_CENTER);
            payment.setBorderColor(BaseColor.WHITE);
            payment.setBackgroundColor(new BaseColor(182, 208, 226));


            headerTable.addCell(account);
            headerTable.addCell(concept);
            headerTable.addCell(charge);
            headerTable.addCell(payment);


            //--------- body
            PdfPTable bodyTable = new PdfPTable(4);
            bodyTable.setWidthPercentage(100);

            PdfPTable cargoTable = new PdfPTable(4);
            cargoTable.setWidthPercentage(100);

            PdfPTable abonoTable = new PdfPTable(4);
            abonoTable.setWidthPercentage(100);


            if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("I") && policyObjFile.getPolicyObj().getMethodPayment().equals("PUE")) {

                if ((policyObjFile.getPolicyObj().getRegimen().equals(RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG625.getRegimen())) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG621.getRegimen()) &&
                                policyObjFile.getPolicyObj().getUsoCFDI().equals(G03.getValue())) {

                    LOGGER.info("131 --- {} . {} . {}  . {} .  {} . {} . {} . {} --- R1 ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());

                    //LOGGER.error(" {}", policyObjFile);


                    if (policyObjFile.getPolicyObj().getDescuento().isEmpty() || policyObjFile.getPolicyObj().getDescuento() == null) {
                        policyObjFile.getPolicyObj().setDescuento("0.00");
                    }

                     double suma = 0;
                    for (int i = 0; i < policyObjFile.getPolicyObj().getAbono().size(); i++) {
                        suma += Double.parseDouble(policyObjFile.getPolicyObj().getAbono().get(i));
                    }

                    //double subtotal = suma - Double.parseDouble(policyObjFile.getPolicyObj().getDescuento());
                    //Double carG = (suma + Double.valueOf(policyObjFile.getPolicyObj().getImpuestos())) - Double.valueOf(policyObjFile.getPolicyObj().getDescuento());
                    //cargoTotal = String.valueOf(carG);
                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getConcepto_Descripcion() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph(CreateFilePDFBalance.decimal(Double.parseDouble(String.valueOf(suma))), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    paymentBody.setBorderColorLeft(BaseColor.WHITE);
                    paymentBody.setBorderColorRight(BaseColor.WHITE);
                    paymentBody.setBorderColorTop(BaseColor.WHITE);
                    paymentBody.setBorderColorBottom(BaseColor.BLACK);
                    paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Body.setBorderColorBottom(BaseColor.BLACK);
                    Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                    bodyTable.addCell(accountBody);
                    bodyTable.addCell(descriptionBody);
                    bodyTable.addCell(paymentBody);
                    bodyTable.addCell(Body);


                    double imp = (Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos()) + suma) - Double.valueOf(policyObjFile.getPolicyObj().getDescuento());
                    impuesto = String.valueOf(imp);
                    cargoTotal = String.valueOf((Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos()) + suma));

                    double sumaTraslado = 0;
                    List<String> deduped = policyObjFile.getPolicyObj().getTraslado().stream().distinct().collect(Collectors.toList());
                    
                    if (!policyObjFile.getTax_id().isEmpty()) {
                        for (int i = 0; i < policyObjFile.getTax_id().size(); i++) {

                            PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            accountCargo.setBorderColorBottom(BaseColor.BLACK);
                            accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                            descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            PdfPCell cargoCargo;
                            if (!policyObjFile.getPolicyObj().getTraslado().isEmpty()) {
                                cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTraslado().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));

                            } else {
                                cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpuestos(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            }

                            cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                            cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell cargo = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            cargo.setBorderColorBottom(BaseColor.BLACK);
                            cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                            cargoTable.addCell(accountCargo);
                            cargoTable.addCell(descripcionCargo);
                            cargoTable.addCell(cargoCargo);
                            cargoTable.addCell(cargo);
                        }
                    }

                    if (!policyObjFile.getPolicyObj().getRetencionId().isEmpty()) {
                        for (int i = 0; i < policyObjFile.getPolicyObj().getRetencionId().size(); i++) {
                            PdfPCell accountAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencionId().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            accountAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell descripcionAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencionDesc().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            descripcionAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);


                            PdfPCell AbonoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            AbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell AbonoPagoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencionPago().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            AbonoPagoPPD.setBorderColorBottom(BaseColor.BLACK);
                            AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            abonoTable.addCell(accountAbonoPPD);
                            abonoTable.addCell(descripcionAbonoPPD);
                            abonoTable.addCell(AbonoPPD);
                            abonoTable.addCell(AbonoPagoPPD);
                        }

                    }

                    PdfPCell accountAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_id(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_descripcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell AbonoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    AbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell AbonoPagoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    AbonoPagoPPD.setBorderColorBottom(BaseColor.BLACK);
                    AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    abonoTable.addCell(accountAbonoPPD);
                    abonoTable.addCell(descripcionAbonoPPD);
                    abonoTable.addCell(AbonoPPD);
                    abonoTable.addCell(AbonoPagoPPD);


                }

            } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("I") && policyObjFile.getPolicyObj().getMetodo().equals("PPD")) {
                if (policyObjFile.getCuenta() == null) {
                    policyObjFile.setCuenta("0");
                }
                LOGGER.info("281 --- {} . {} . {}  . {} .  {} . {} . {} . {} --- ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());
                //LOGGER.error(" {}", policyObjFile);
                if (policyObjFile.getPolicyObj().getClaveProdServ().get(0).equals("78101801")) {
                    policyObjFile.setCuenta("501.01");
                    policyObjFile.getPolicyObj().setConcepto_Descripcion("Costo de venta");

                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getConcepto_Descripcion() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    paymentBody.setBorderColorLeft(BaseColor.WHITE);
                    paymentBody.setBorderColorRight(BaseColor.WHITE);
                    paymentBody.setBorderColorTop(BaseColor.WHITE);
                    paymentBody.setBorderColorBottom(BaseColor.BLACK);
                    paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Body.setBorderColorBottom(BaseColor.BLACK);
                    Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                    bodyTable.addCell(accountBody);
                    bodyTable.addCell(descriptionBody);
                    bodyTable.addCell(paymentBody);
                    bodyTable.addCell(Body);

                    PdfPCell accountCargo = new PdfPCell(new Paragraph("503.01", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountCargo.setBorderColorBottom(BaseColor.BLACK);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionCargo = new PdfPCell(new Paragraph("Devoluciones, descuentos o bonificaciones sobre compras", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoCargo = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell cargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargo.setBorderColorBottom(BaseColor.BLACK);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    cargoTable.addCell(accountCargo);
                    cargoTable.addCell(descripcionCargo);
                    cargoTable.addCell(cargoCargo);
                    cargoTable.addCell(cargo);


                    PdfPCell accountAbono = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbono.setBorderColorBottom(BaseColor.BLACK);
                    accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbono = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell Abono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTraslado().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Abono.setBorderColorBottom(BaseColor.BLACK);
                    Abono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    Abono.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell AbonoPago = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    AbonoPago.setBorderColorBottom(BaseColor.BLACK);
                    AbonoPago.setHorizontalAlignment(Element.ALIGN_CENTER);
                    AbonoPago.setHorizontalAlignment(Element.ALIGN_CENTER);

                    abonoTable.addCell(accountAbono);
                    abonoTable.addCell(descripcionAbono);
                    abonoTable.addCell(Abono);
                    abonoTable.addCell(AbonoPago);


                    PdfPCell accountAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell AbonoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    AbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                    AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell AbonoPagoPPD = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTraslado().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    AbonoPagoPPD.setBorderColorBottom(BaseColor.BLACK);
                    AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                    AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                    abonoTable.addCell(accountAbonoPPD);
                    abonoTable.addCell(descripcionAbonoPPD);
                    abonoTable.addCell(AbonoPPD);
                    abonoTable.addCell(AbonoPagoPPD);


                } else {

                    double suma = 0;
                    for (int i = 0; i < policyObjFile.getPolicyObj().getAbono().size(); i++) {
                        suma += Double.parseDouble(policyObjFile.getPolicyObj().getAbono().get(i));
                    }

                    Double carG = suma + Double.valueOf(policyObjFile.getPolicyObj().getImpuestos());
                    cargoTotal = String.valueOf(carG);
                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getConcepto_Descripcion() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph(String.valueOf(suma), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    paymentBody.setBorderColorLeft(BaseColor.WHITE);
                    paymentBody.setBorderColorRight(BaseColor.WHITE);
                    paymentBody.setBorderColorTop(BaseColor.WHITE);
                    paymentBody.setBorderColorBottom(BaseColor.BLACK);
                    paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Body.setBorderColorBottom(BaseColor.BLACK);
                    Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                    bodyTable.addCell(accountBody);
                    bodyTable.addCell(descriptionBody);
                    bodyTable.addCell(paymentBody);
                    bodyTable.addCell(Body);


                    PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountCargo.setBorderColorBottom(BaseColor.BLACK);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpuestos(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell cargo = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargo.setBorderColorBottom(BaseColor.BLACK);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    cargoTable.addCell(accountCargo);
                    cargoTable.addCell(descripcionCargo);
                    cargoTable.addCell(cargoCargo);
                    cargoTable.addCell(cargo);


                    double imp = Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos()) + suma;
                    impuesto = String.valueOf(imp);

                    if (!policyObjFile.getTax_id().isEmpty()) {
                        for (int i = 0; i < policyObjFile.getTax_id().size(); i++) {

                            PdfPCell accountAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            accountAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell descripcionAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            descripcionAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);


                            PdfPCell AbonoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            AbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                            AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            PdfPCell AbonoPagoPPD = new PdfPCell(new Paragraph(String.valueOf(imp), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                            AbonoPagoPPD.setBorderColorBottom(BaseColor.BLACK);
                            AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                            AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                            abonoTable.addCell(accountAbonoPPD);
                            abonoTable.addCell(descripcionAbonoPPD);
                            abonoTable.addCell(AbonoPPD);
                            abonoTable.addCell(AbonoPagoPPD);
                        }
                    }
                }

            } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("E")) {
                if ((policyObjFile.getPolicyObj().getRegimen().equals(RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG624.getRegimen())) &&
                        policyObjFile.getPolicyObj().getUsoCFDI().equals(G03.getValue()) ||
                        policyObjFile.getPolicyObj().getUsoCFDI().equals(G02.getValue())) {

                    if (policyObjFile.getCuenta() == null) {
                        policyObjFile.setCuenta("0");
                    }

                    LOGGER.info("507  --- {} . {} . {}  . {} .  {} . {} . {} . {} --- ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());
                    //LOGGER.error("{}", policyObjFile);
                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    paymentBody.setBorderColorLeft(BaseColor.WHITE);
                    paymentBody.setBorderColorRight(BaseColor.WHITE);
                    paymentBody.setBorderColorTop(BaseColor.WHITE);
                    paymentBody.setBorderColorBottom(BaseColor.BLACK);
                    paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Body.setBorderColorBottom(BaseColor.BLACK);
                    Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                    bodyTable.addCell(accountBody);
                    bodyTable.addCell(descriptionBody);
                    bodyTable.addCell(paymentBody);
                    bodyTable.addCell(Body);


                }
            } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("N")) {
                if (policyObjFile.getPolicyObj().getRegimen().equals(RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG605.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(RG624.getRegimen()) &&
                                policyObjFile.getPolicyObj().getUsoCFDI().equals(CN01.getValue())) {
                    //revisar
                    //LOGGER.error("N  {}", policyObjFile);
                    if (policyObjFile.getCuenta() == null) {
                        policyObjFile.setCuenta("0");
                    }
                    LOGGER.info("552 no --- {} . {} . {}  . {} .  {} . {} . {} . {} --- R9 ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());
                    //LOGGER.error("{}", policyObjFile);
                    if (policyObjFile.getPolicyObj().getClaveProdServ().get(0).equals("84111505")) {
                        //pdf creation
                        PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getCuenta(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountBody.setBorderColorBottom(BaseColor.BLACK);
                        accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getDescripcion() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                        descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        paymentBody.setBorderColorLeft(BaseColor.WHITE);
                        paymentBody.setBorderColorRight(BaseColor.WHITE);
                        paymentBody.setBorderColorTop(BaseColor.WHITE);
                        paymentBody.setBorderColorBottom(BaseColor.BLACK);
                        paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        Body.setBorderColorBottom(BaseColor.BLACK);
                        Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                        bodyTable.addCell(accountBody);
                        bodyTable.addCell(descriptionBody);
                        bodyTable.addCell(paymentBody);
                        bodyTable.addCell(Body);


                        PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getAbonoIMSS(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountCargo.setBorderColorBottom(BaseColor.BLACK);
                        accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getAbonoDescipcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                        descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell cargoCargo = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                        cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell cargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getPercepciones().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        cargo.setBorderColorBottom(BaseColor.BLACK);
                        cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                        cargoTable.addCell(accountCargo);
                        cargoTable.addCell(descripcionCargo);
                        cargoTable.addCell(cargoCargo);
                        cargoTable.addCell(cargo);


                        PdfPCell accountAbono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getIsr(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountAbono.setBorderColorBottom(BaseColor.BLACK);
                        accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell descripcionAbono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getIsrDescripcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
                        descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell Abono = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        Abono.setBorderColorBottom(BaseColor.BLACK);
                        Abono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        Abono.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell AbonoPago = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTypeOfEgresoN().getPercepciones().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        AbonoPago.setBorderColorBottom(BaseColor.BLACK);
                        AbonoPago.setHorizontalAlignment(Element.ALIGN_CENTER);
                        AbonoPago.setHorizontalAlignment(Element.ALIGN_CENTER);

                        abonoTable.addCell(accountAbono);
                        abonoTable.addCell(descripcionAbono);
                        abonoTable.addCell(Abono);
                        abonoTable.addCell(AbonoPago);

                    }

                }
            } else if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("P")) {
                if (policyObjFile.getCuenta() == null) {
                    policyObjFile.setCuenta("0");
                }
                if (policyObjFile.getPolicyObj().getAmount() == null || policyObjFile.getPolicyObj().getAmount().isEmpty()) {
                    policyObjFile.getPolicyObj().setAmount("0.0");
                }
                //revisar
                LOGGER.info("647 no --- {} . {} . {}  . {} .  {} . {} . {} . {} --- ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());
                //LOGGER.error("{}", policyObjFile);
                PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountBody.setBorderColorBottom(BaseColor.BLACK);
                accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpPagado(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                paymentBody.setBorderColorLeft(BaseColor.WHITE);
                paymentBody.setBorderColorRight(BaseColor.WHITE);
                paymentBody.setBorderColorTop(BaseColor.WHITE);
                paymentBody.setBorderColorBottom(BaseColor.BLACK);
                paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell Body = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                Body.setBorderColorBottom(BaseColor.BLACK);
                Body.setHorizontalAlignment(Element.ALIGN_CENTER);


                bodyTable.addCell(accountBody);
                bodyTable.addCell(descriptionBody);
                bodyTable.addCell(paymentBody);
                bodyTable.addCell(Body);

                if (!policyObjFile.getTax_id().isEmpty()) {
                    for (int i = 0; i < policyObjFile.getTax_id().size(); i++) {

                        PdfPCell accountAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                        accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                        accountAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell descripcionAbonoPPD = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descripcionAbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                        descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                        descripcionAbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell AbonoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        AbonoPPD.setBorderColorBottom(BaseColor.BLACK);
                        AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                        AbonoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell AbonoPagoPPD = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        AbonoPagoPPD.setBorderColorBottom(BaseColor.BLACK);
                        AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);
                        AbonoPagoPPD.setHorizontalAlignment(Element.ALIGN_CENTER);

                        abonoTable.addCell(accountAbonoPPD);
                        abonoTable.addCell(descripcionAbonoPPD);
                        abonoTable.addCell(AbonoPPD);
                        abonoTable.addCell(AbonoPagoPPD);
                    }
                }
                cargoTotal = "0.0";
                impuesto = "0.0";
            }


            //---------------------   footer of file  --------------------- //

            PdfPTable footer = new PdfPTable(3);

            PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
            sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));
            PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + cargoTotal, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + impuesto, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            sumAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

            footer.addCell(sumFooter);
            footer.addCell(sumCargo);
            footer.addCell(sumAbono);

            footer.setTotalWidth(523);
            footer.setHorizontalAlignment(2);
            footer.setWidthPercentage(60);


            //---------------------   last values of file  --------------------- //


            PdfPTable headerLastValues = new PdfPTable(4);
            headerLastValues.setWidthPercentage(100);

            PdfPCell cellDate = new PdfPCell(new Paragraph("Fecha", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDate.setBorderColor(BaseColor.WHITE);
            cellDate.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellConcept = new PdfPCell(new Paragraph("Proveedor / Cliente / Nómina", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellConcept.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellConcept.setBorderColor(BaseColor.WHITE);
            cellConcept.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellFolio = new PdfPCell(new Paragraph("Folio Fiscal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellFolio.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellFolio.setBorderColor(BaseColor.WHITE);
            cellFolio.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell cellTotal = new PdfPCell(new Paragraph("Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTotal.setBorderColor(BaseColor.WHITE);
            cellTotal.setBackgroundColor(new BaseColor(182, 208, 226));

            headerLastValues.addCell(cellDate);
            headerLastValues.addCell(cellConcept);
            headerLastValues.addCell(cellFolio);
            headerLastValues.addCell(cellTotal);


            PdfPTable lastValues = new PdfPTable(4);

            lastValues.setWidthPercentage(100);

            PdfPCell cellUUID = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cellUUID.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellUUID.setBorderColor(BaseColor.BLACK);

            PdfPCell totalAmount = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            totalAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalAmount.setBorderColor(BaseColor.BLACK);


            PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            provedor.setHorizontalAlignment(Element.ALIGN_CENTER);
            provedor.setBorderColor(BaseColor.BLACK);


            PdfPCell end_date = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            end_date.setHorizontalAlignment(Element.ALIGN_CENTER);
            end_date.setBorderColor(BaseColor.BLACK);


            lastValues.addCell(end_date);
            lastValues.addCell(provedor);
            lastValues.addCell(cellUUID);
            lastValues.addCell(totalAmount);


            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(headerTable);
            document.add(bodyTable);
            document.add(cargoTable);
            document.add(abonoTable);
            document.add(footer);
            document.add(new Paragraph("\n\n\n\n"));
            document.add(headerLastValues);
            document.add(lastValues);
            document.close();

            UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);

            return true;
        } catch (Exception e) {
            LOGGER.error("CreateFilePDFPolicy makeFileEgreso {} . {} . {}", e.getMessage(), e.getCause(), e.getLocalizedMessage());
        }
        return false;
    }

    public static boolean makeFileIngreso(PolicyObjFile policyObjFile, String fileName, String rfc, String type) {

        try {

            File uploadDir = new File(server_path + rfc + "/pdf/" + type + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(server_path + rfc + "/pdf/" + type + "/" + fileName + ".pdf"));
            document.open();
            LOGGER.info("827 --- {} . {} . {}  . {} .  {} . {} . {} . {} --- ", type, policyObjFile.getPolicyObj().getTypeOfComprobante(), policyObjFile.getPolicyObj().getMethodPayment(), policyObjFile.getPolicyObj().getRegimen(), policyObjFile.getPolicyObj().getUsoCFDI(), policyObjFile.getCuenta_method(), policyObjFile.getTax_id(), policyObjFile.getCuenta());
            //LOGGER.error("{}", policyObjFile);
            //---------------------   header of file  --------------------- //

            PdfPTable table = new PdfPTable(2);

            PdfPCell client = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            client.setHorizontalAlignment(Element.ALIGN_CENTER);
            client.setBorderColor(BaseColor.WHITE);
            client.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell folio = new PdfPCell(new Paragraph("P ó l i z a" + "\n" + " Tipo: " + policyObjFile.getTypeOf() + "  Folio: " + policyObjFile.getFolio(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK.darker())));
            folio.setHorizontalAlignment(Element.ALIGN_CENTER);
            folio.setBorderColor(BaseColor.WHITE);
            folio.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell company = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            company.setHorizontalAlignment(Element.ALIGN_CENTER);
            company.setBorderColor(BaseColor.WHITE);
            company.setBackgroundColor(new BaseColor(229, 231, 233));


            PdfPCell date_ = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
            date_.setHorizontalAlignment(Element.ALIGN_CENTER);
            date_.setBorderColor(BaseColor.WHITE);
            date_.setBackgroundColor(new BaseColor(229, 231, 233));


            table.addCell(client);
            table.addCell(folio);
            table.addCell(company);
            table.addCell(date_);
            table.setHorizontalAlignment(2);
            table.setWidthPercentage(60);

            PdfPTable headerTable = new PdfPTable(4);
            headerTable.setWidthPercentage(100);

            PdfPCell account = new PdfPCell(new Paragraph("Cuenta", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            account.setHorizontalAlignment(Element.ALIGN_CENTER);
            account.setBorderColor(BaseColor.WHITE);
            account.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell concept = new PdfPCell(new Paragraph("Concepto", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            concept.setHorizontalAlignment(Element.ALIGN_CENTER);
            concept.setPaddingRight(40);
            concept.setBorderColor(BaseColor.WHITE);
            concept.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell charge = new PdfPCell(new Paragraph("Cargo", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            charge.setHorizontalAlignment(Element.ALIGN_CENTER);
            charge.setBorderColor(BaseColor.WHITE);
            charge.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell payment = new PdfPCell(new Paragraph("Abono", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            payment.setHorizontalAlignment(Element.ALIGN_CENTER);
            payment.setBorderColor(BaseColor.WHITE);
            payment.setBackgroundColor(new BaseColor(182, 208, 226));


            headerTable.addCell(account);
            headerTable.addCell(concept);
            headerTable.addCell(charge);
            headerTable.addCell(payment);


            PdfPTable bodyTable = new PdfPTable(4);
            bodyTable.setWidthPercentage(100);


            PdfPTable abonoTable = new PdfPTable(4);
            abonoTable.setWidthPercentage(100);

// ------------------------------------------------------------------------------------------------------------------------
            if (policyObjFile.getPolicyObj().getTypeOfComprobante().equals("N")) {

                // for (int j = 0; j < policyObjFile.getTax_id().size(); j++) {

                PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));

                accountBody.setBorderColorBottom(BaseColor.BLACK);
                accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                paymentBody.setBorderColorLeft(BaseColor.WHITE);
                paymentBody.setBorderColorRight(BaseColor.WHITE);
                paymentBody.setBorderColorTop(BaseColor.WHITE);
                paymentBody.setBorderColorBottom(BaseColor.BLACK);
                paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell Body = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                Body.setBorderColorBottom(BaseColor.BLACK);
                Body.setHorizontalAlignment(Element.ALIGN_CENTER);

                bodyTable.addCell(accountBody);
                bodyTable.addCell(descriptionBody);
                bodyTable.addCell(paymentBody);
                bodyTable.addCell(Body);

                //}

                if (!policyObjFile.getTax_id().isEmpty()) {
                    for (int i = 0; i < policyObjFile.getTax_id().size(); i++) {


                        PdfPCell accountAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountAbono.setBorderColorBottom(BaseColor.BLACK);
                        accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell descripcionAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
                        descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell cargoAbono = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        cargoAbono.setBorderColorBottom(BaseColor.BLACK);
                        cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell abono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(i), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        abono.setBorderColorBottom(BaseColor.BLACK);
                        abono.setHorizontalAlignment(Element.ALIGN_CENTER);
                        abono.setHorizontalAlignment(Element.ALIGN_CENTER);


                        abonoTable.addCell(accountAbono);
                        abonoTable.addCell(descripcionAbono);
                        abonoTable.addCell(cargoAbono);
                        abonoTable.addCell(abono);

                    }
                }

            } else {

                for (int j = 0; j < policyObjFile.getTax_id().size(); j++) {
                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(j) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));

                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph("0.0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    paymentBody.setBorderColorLeft(BaseColor.WHITE);
                    paymentBody.setBorderColorRight(BaseColor.WHITE);
                    paymentBody.setBorderColorTop(BaseColor.WHITE);
                    paymentBody.setBorderColorBottom(BaseColor.BLACK);
                    paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell Body = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    Body.setBorderColorBottom(BaseColor.BLACK);
                    Body.setHorizontalAlignment(Element.ALIGN_CENTER);

                    bodyTable.addCell(accountBody);
                    bodyTable.addCell(descriptionBody);
                    bodyTable.addCell(paymentBody);
                    bodyTable.addCell(Body);

                }
            }
// ------------------------------------------------------------------------------------------------------------------------


            //------------ footer


            PdfPTable footer = new PdfPTable(3);

            PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
            sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos())), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos())), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            sumAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


            footer.addCell(sumFooter);
            footer.addCell(sumCargo);
            footer.addCell(sumAbono);


            footer.setTotalWidth(523);
            footer.setHorizontalAlignment(2);
            footer.setWidthPercentage(60);
            //------------ lastValues


            PdfPTable headerLastValues = new PdfPTable(4);
            headerLastValues.setWidthPercentage(100);

            PdfPCell cellDate = new PdfPCell(new Paragraph("Fecha", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellDate.setBorderColor(BaseColor.WHITE);
            cellDate.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellConcept = new PdfPCell(new Paragraph("Proveedor / Cliente / Nómina", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellConcept.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellConcept.setBorderColor(BaseColor.WHITE);
            cellConcept.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellFolio = new PdfPCell(new Paragraph("Folio Fiscal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellFolio.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellFolio.setBorderColor(BaseColor.WHITE);
            cellFolio.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell cellTotal = new PdfPCell(new Paragraph("Total", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTotal.setBorderColor(BaseColor.WHITE);
            cellTotal.setBackgroundColor(new BaseColor(182, 208, 226));

            headerLastValues.addCell(cellDate);
            headerLastValues.addCell(cellConcept);
            headerLastValues.addCell(cellFolio);
            headerLastValues.addCell(cellTotal);


            PdfPTable lastValues = new PdfPTable(4);

            lastValues.setWidthPercentage(100);

            PdfPCell cellUUID = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            cellUUID.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellUUID.setBorderColor(BaseColor.BLACK);

            PdfPCell totalAmount = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            totalAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalAmount.setBorderColor(BaseColor.BLACK);


            PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            provedor.setHorizontalAlignment(Element.ALIGN_CENTER);
            provedor.setBorderColor(BaseColor.BLACK);


            PdfPCell end_date = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
            end_date.setHorizontalAlignment(Element.ALIGN_CENTER);
            end_date.setBorderColor(BaseColor.BLACK);


            lastValues.addCell(end_date);
            lastValues.addCell(provedor);
            lastValues.addCell(cellUUID);
            lastValues.addCell(totalAmount);

            /* Closing document */
            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(headerTable);
            document.add(bodyTable);
            document.add(abonoTable);
            document.add(footer);
            document.add(new Paragraph("\n\n\n\n"));
            document.add(headerLastValues);
            document.add(lastValues);
            document.close();
            /* Uploading file to S3*/
            UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);
            return true;

        } catch (Exception e) {
            LOGGER.error("CreateFilePDFPolicy makeFileIngreso {} ", e.getMessage());
        }
        return false;
    }
}
