package org.example.users.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.users.model.PolicyObjFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import static org.example.users.util.CFDI.*;
import static org.example.users.util.Regimen.*;


public class CreateFilePDFPolicy {

    //private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all2/";
    private static String server_path = "/home/ubuntu/endpoints/eTribute-all/";

    public CreateFilePDFPolicy() {
    }


    public static boolean makeFileEgreso(PolicyObjFile policyObjFile, String fileName, String rfc, String type) {


        try {

            File uploadDir = new File(server_path + rfc + "/pdf/" + type + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(server_path + rfc + "/pdf/" + type + "/" + fileName + ".pdf"));
            document.open();


            //---------------------   header of file  --------------------- //

            PdfPTable table = new PdfPTable(2);

            PdfPCell cell1 = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell cell2 = new PdfPCell(new Paragraph("P ó l i z a" + "\n" + " Tipo: " + policyObjFile.getTypeOf() + "  Folio: " + policyObjFile.getFolio(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK.darker())));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorderColor(BaseColor.WHITE);
            cell2.setBackgroundColor(new BaseColor(182, 208, 226));

            //get month of query
            PdfPCell cell3 = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderColor(BaseColor.WHITE);
            cell3.setBackgroundColor(new BaseColor(229, 231, 233));


            //param initialDate and endDate
            PdfPCell cell4 = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderColor(BaseColor.WHITE);
            cell4.setBackgroundColor(new BaseColor(229, 231, 233));


            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);


            table.setHorizontalAlignment(2);
            table.setWidthPercentage(60);


            PdfPTable headerTable = new PdfPTable(4);

            headerTable.setWidthPercentage(100);

            PdfPCell cellBody = new PdfPCell(new Paragraph("Cuenta", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellBody.setBorderColor(BaseColor.WHITE);
            cellBody.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellName = new PdfPCell(new Paragraph("Concepto", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellName.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellName.setPaddingRight(40);
            cellName.setBorderColor(BaseColor.WHITE);
            cellName.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell cellCharge = new PdfPCell(new Paragraph("Cargo", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellCharge.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCharge.setBorderColor(BaseColor.WHITE);
            cellCharge.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellAb = new PdfPCell(new Paragraph("Abono", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellAb.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAb.setBorderColor(BaseColor.WHITE);
            cellAb.setBackgroundColor(new BaseColor(182, 208, 226));


            headerTable.addCell(cellBody);
            headerTable.addCell(cellName);
            headerTable.addCell(cellCharge);
            headerTable.addCell(cellAb);


            //---------------------   body of file  --------------------- //

            PdfPTable bodyTable = new PdfPTable(4);

            bodyTable.setWidthPercentage(100);


            double totalR = 0;
            double total = 0;

            if (!policyObjFile.getPolicyObj().getMetodo().equals("PPD")) {
                if (!policyObjFile.getPolicyObj().getRetencion_importe().isEmpty() || policyObjFile.getPolicyObj().getRetencion_importe() != null) {
                    for (int i = 0; i < policyObjFile.getPolicyObj().getRetencion_importe().size(); i++) {
                        totalR += Double.parseDouble(policyObjFile.getPolicyObj().getRetencion_importe().get(i).isEmpty() || policyObjFile.getPolicyObj().getRetencion_importe().get(i) == null ? "0" : policyObjFile.getPolicyObj().getRetencion_importe().get(i));
                    }
                    total = totalR + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos());

                }
            }


            if (policyObjFile.getPolicyObj().getType_of_value().equals("I") && policyObjFile.getPolicyObj().getMetodo().equals("PUE")) {


                if ((policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG625.getRegimen())) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG621.getRegimen()) &&
                                policyObjFile.getPolicyObj().getUsoCFDI().equals(G03.getValue())) {


                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_id(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_descripcion() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                    //---------------------   charger of file  --------------------- //

                    // ----------- cargoTable
                    PdfPTable cargoTable = new PdfPTable(4);
                    cargoTable.setWidthPercentage(100);

                    PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountCargo.setBorderColorBottom(BaseColor.BLACK);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(3), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargo.setBorderColorBottom(BaseColor.BLACK);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    cargoTable.addCell(accountCargo);
                    cargoTable.addCell(descripcionCargo);
                    cargoTable.addCell(cargoCargo);
                    cargoTable.addCell(cargo);


                    //---------------------   cargo of file  --------------------- //


                    PdfPTable abonoTable = new PdfPTable(4);
                    abonoTable.setWidthPercentage(100);


                    PdfPCell accountAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbono.setBorderColorBottom(BaseColor.BLACK);
                    accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoAbono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(2), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoAbono.setBorderColorBottom(BaseColor.BLACK);
                    cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell abono = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    abono.setBorderColorBottom(BaseColor.BLACK);
                    abono.setHorizontalAlignment(Element.ALIGN_CENTER);
                    abono.setHorizontalAlignment(Element.ALIGN_CENTER);


                    abonoTable.addCell(accountAbono);
                    abonoTable.addCell(descripcionAbono);
                    abonoTable.addCell(cargoAbono);
                    abonoTable.addCell(abono);


                    //---------------------   abono of file  --------------------- //


                    PdfPTable abonoTable2 = new PdfPTable(4);
                    abonoTable2.setWidthPercentage(100);


                    PdfPCell accountAbono2 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAbono().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbono2.setBorderColorBottom(BaseColor.BLACK);
                    accountAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbono2 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCargo().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbono2.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoAbono2 = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoAbono2.setBorderColorBottom(BaseColor.BLACK);
                    cargoAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoAbono2.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell abono2 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    abono2.setBorderColorBottom(BaseColor.BLACK);
                    abono2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    abono2.setHorizontalAlignment(Element.ALIGN_CENTER);


                    abonoTable2.addCell(accountAbono2);
                    abonoTable2.addCell(descripcionAbono2);
                    abonoTable2.addCell(cargoAbono2);
                    abonoTable2.addCell(abono2);


                    PdfPTable abonoTable3 = new PdfPTable(4);
                    abonoTable3.setWidthPercentage(100);


                    PdfPCell accountAbono3 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAbono().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountAbono3.setBorderColorBottom(BaseColor.BLACK);
                    accountAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    accountAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descripcionAbono3 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getCargo().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descripcionAbono3.setBorderColorBottom(BaseColor.BLACK);
                    descripcionAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    descripcionAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell cargoAbono3 = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    cargoAbono3.setBorderColorBottom(BaseColor.BLACK);
                    cargoAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cargoAbono3.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell abono3 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpuestos(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    abono3.setBorderColorBottom(BaseColor.BLACK);
                    abono3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    abono3.setHorizontalAlignment(Element.ALIGN_CENTER);


                    abonoTable3.addCell(accountAbono3);
                    abonoTable3.addCell(descripcionAbono3);
                    abonoTable3.addCell(cargoAbono3);
                    abonoTable3.addCell(abono3);


                    //---------------------   footer of file  --------------------- //

                    PdfPTable footer = new PdfPTable(3);

                    PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                    sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


                    PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getTotalAmount()) + Double.parseDouble(policyObjFile.getPolicyObj().getRetencion_importe().get(2)) + Double.parseDouble(policyObjFile.getPolicyObj().getRetencion_importe().get(3))), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos())), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                    PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                    provedor.setHorizontalAlignment(Element.ALIGN_CENTER);
                    provedor.setBorderColor(BaseColor.BLACK);


                    PdfPCell end_date = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                    end_date.setHorizontalAlignment(Element.ALIGN_CENTER);
                    end_date.setBorderColor(BaseColor.BLACK);


                    lastValues.addCell(end_date);
                    lastValues.addCell(provedor);
                    lastValues.addCell(cellUUID);
                    lastValues.addCell(totalAmount);


                    //---------------------   adding tables of file  --------------------- //

                    document.add(table);
                    document.add(new Paragraph("\n"));
                    document.add(headerTable);
                    document.add(bodyTable);
                    document.add(cargoTable);
                    document.add(abonoTable);
                    document.add(abonoTable2);
                    document.add(abonoTable3);
                    document.add(footer);
                    document.add(new Paragraph("\n\n\n\n"));
                    document.add(headerLastValues);
                    document.add(lastValues);
                    document.close();

                    UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);


                }
                // -----------------------  Type of policy I and PPD   --------------------- //

            } else if (policyObjFile.getPolicyObj().getType_of_value().equals("I") && policyObjFile.getPolicyObj().getMetodo().equals("PPD")) {


                PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountBody.setBorderColorBottom(BaseColor.BLACK);
                accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getTax_description() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                PdfPTable cargoTable = new PdfPTable(4);
                cargoTable.setWidthPercentage(100);

                PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountCargo.setBorderColorBottom(BaseColor.BLACK);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoCargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargo = new PdfPCell(new Paragraph(String.valueOf(policyObjFile.getPolicyObj().getSubtotal()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargo.setBorderColorBottom(BaseColor.BLACK);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                cargoTable.addCell(accountCargo);
                cargoTable.addCell(descripcionCargo);
                cargoTable.addCell(cargoCargo);
                cargoTable.addCell(cargo);



                PdfPTable Table = new PdfPTable(4);
                Table.setWidthPercentage(100);

                PdfPCell ta = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_id(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                ta.setBorderColorBottom(BaseColor.BLACK);
                ta.setHorizontalAlignment(Element.ALIGN_CENTER);
                ta.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell iva = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_descripcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                iva.setBorderColorBottom(BaseColor.BLACK);
                iva.setHorizontalAlignment(Element.ALIGN_CENTER);
                iva.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell ca = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                ca.setBorderColorBottom(BaseColor.BLACK);
                ca.setHorizontalAlignment(Element.ALIGN_CENTER);
                ca.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoT = new PdfPCell(new Paragraph(String.valueOf(policyObjFile.getPolicyObj().getTraslado().get(0)), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoT.setBorderColorBottom(BaseColor.BLACK);
                cargoT.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoT.setHorizontalAlignment(Element.ALIGN_CENTER);

                Table.addCell(ta);
                Table.addCell(iva);
                Table.addCell(ca);
                Table.addCell(cargoT);


                //--- iva


                PdfPTable TableIva = new PdfPTable(4);
                TableIva.setWidthPercentage(100);

                int j = 0;
                for (Map.Entry<String, String> i : policyObjFile.getPolicyObj().getIva().entrySet()) {


                    PdfPCell id = new PdfPCell(new Paragraph(i.getKey(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    id.setBorderColorBottom(BaseColor.BLACK);
                    id.setHorizontalAlignment(Element.ALIGN_CENTER);
                    id.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell a = new PdfPCell(new Paragraph(i.getValue(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    a.setBorderColorBottom(BaseColor.BLACK);
                    a.setHorizontalAlignment(Element.ALIGN_CENTER);
                    a.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell car = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    car.setBorderColorBottom(BaseColor.BLACK);
                    car.setHorizontalAlignment(Element.ALIGN_CENTER);
                    car.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell carg = new PdfPCell(new Paragraph("0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    carg.setBorderColorBottom(BaseColor.BLACK);
                    carg.setHorizontalAlignment(Element.ALIGN_CENTER);
                    carg.setHorizontalAlignment(Element.ALIGN_CENTER);

                    TableIva.addCell(id);
                    TableIva.addCell(a);
                    TableIva.addCell(car);
                    TableIva.addCell(carg);
                    j++;
                }

                //------------ footer

                PdfPTable footer = new PdfPTable(3);

                PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
                sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


                PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
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
                document.add(TableIva);
                document.add(cargoTable);
                document.add(Table);
                document.add(footer);
                document.add(new Paragraph("\n\n\n\n"));
                document.add(headerLastValues);
                document.add(lastValues);
                document.close();
                UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);


            } else if (policyObjFile.getPolicyObj().getType_of_value().equals("E")) {
                if ((policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG624.getRegimen())) &&
                        policyObjFile.getPolicyObj().getUsoCFDI().equals(G03.getValue())) {


                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));

                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph("" + policyObjFile.getPolicyObj().getSubtotal(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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

                // ----------- cargoTable
                PdfPTable cargoTable = new PdfPTable(4);
                cargoTable.setWidthPercentage(100);

                PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_id(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountCargo.setBorderColorBottom(BaseColor.BLACK);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_descripcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpuestos(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargo.setBorderColorBottom(BaseColor.BLACK);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                cargoTable.addCell(accountCargo);
                cargoTable.addCell(descripcionCargo);
                cargoTable.addCell(cargoCargo);
                cargoTable.addCell(cargo);


                //------- abonoTable

                PdfPTable abonoTable = new PdfPTable(4);
                abonoTable.setWidthPercentage(100);


                PdfPCell accountAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountAbono.setBorderColorBottom(BaseColor.BLACK);
                accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionAbono = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
                descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoAbono = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoAbono.setBorderColorBottom(BaseColor.BLACK);
                cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell abono = new PdfPCell(new Paragraph("" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                abono.setBorderColorBottom(BaseColor.BLACK);
                abono.setHorizontalAlignment(Element.ALIGN_CENTER);
                abono.setHorizontalAlignment(Element.ALIGN_CENTER);


                abonoTable.addCell(accountAbono);
                abonoTable.addCell(descripcionAbono);
                abonoTable.addCell(cargoAbono);
                abonoTable.addCell(abono);


                //------------ footer

                PdfPTable footer = new PdfPTable(3);

                PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
                sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


                PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getTotalAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
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

                // -----------------------  Type of policy P  and N --------------------- //

            } else if (policyObjFile.getPolicyObj().getType_of_value().equals("N")) {


                if (policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG601.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG603.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG605.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG612.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG620.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG622.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG623.getRegimen()) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(Regimen.RG624.getRegimen()) &&
                                policyObjFile.getPolicyObj().getUsoCFDI().equals(CN01.getValue())) {


                    PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                    // ----------- cargoTable
                    PdfPTable cargoTable = new PdfPTable(4);
                    cargoTable.setWidthPercentage(100);


                    for (int j = 1; j < policyObjFile.getTax_id().size(); j++) {

                        PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        accountCargo.setBorderColorBottom(BaseColor.BLACK);
                        accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                        descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell cargoCargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                        cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                        PdfPCell cargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        cargo.setBorderColorBottom(BaseColor.BLACK);
                        cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                        cargoTable.addCell(accountCargo);
                        cargoTable.addCell(descripcionCargo);
                        cargoTable.addCell(cargoCargo);
                        cargoTable.addCell(cargo);

                    }


                    //------------ footer

                    PdfPTable footer = new PdfPTable(3);

                    PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                    sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


                    PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                    PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                    PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
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
                    document.add(footer);
                    document.add(new Paragraph("\n\n\n\n"));
                    document.add(headerLastValues);
                    document.add(lastValues);


                    document.close();
                    UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);

                }
            } else if (policyObjFile.getPolicyObj().getMetodo().equals("P")) {

                PdfPCell accountBody = new PdfPCell(new Paragraph(policyObjFile.getCuenta_method(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountBody.setBorderColorBottom(BaseColor.BLACK);

                PdfPCell descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getDescription_methods() + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell paymentBody = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


                // ----------- cargoTable
                PdfPTable cargoTable = new PdfPTable(4);
                cargoTable.setWidthPercentage(100);


                PdfPCell accountCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountCargo.setBorderColorBottom(BaseColor.BLACK);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionCargo = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoCargo = new PdfPCell(new Paragraph("" + policyObjFile.getPolicyObj().getSubtotal(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoCargo.setBorderColorBottom(BaseColor.BLACK);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargo.setBorderColorBottom(BaseColor.BLACK);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell accountCargo2 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_id(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountCargo2.setBorderColorBottom(BaseColor.BLACK);
                accountCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionCargo2 = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getVenta_descripcion(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionCargo2.setBorderColorBottom(BaseColor.BLACK);
                descripcionCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoCargo2 = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoCargo2.setBorderColorBottom(BaseColor.BLACK);
                cargoCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoCargo2.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargo2 = new PdfPCell(new Paragraph("" + policyObjFile.getPolicyObj().getSubtotal(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargo2.setBorderColorBottom(BaseColor.BLACK);
                cargo2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargo2.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell accountCargo3 = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                accountCargo3.setBorderColorBottom(BaseColor.BLACK);
                accountCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);
                accountCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell descripcionCargo3 = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(1), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                descripcionCargo3.setBorderColorBottom(BaseColor.BLACK);
                descripcionCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);
                descripcionCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargoCargo3 = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargoCargo3.setBorderColorBottom(BaseColor.BLACK);
                cargoCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargoCargo3.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell cargo3 = new PdfPCell(new Paragraph("" + policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                cargo3.setBorderColorBottom(BaseColor.BLACK);
                cargo3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cargo3.setHorizontalAlignment(Element.ALIGN_CENTER);


                cargoTable.addCell(accountCargo);
                cargoTable.addCell(descripcionCargo);
                cargoTable.addCell(cargoCargo);
                cargoTable.addCell(cargo);


                cargoTable.addCell(accountCargo2);
                cargoTable.addCell(descripcionCargo2);
                cargoTable.addCell(cargoCargo2);
                cargoTable.addCell(cargo2);


                cargoTable.addCell(accountCargo3);
                cargoTable.addCell(descripcionCargo3);
                cargoTable.addCell(cargoCargo3);
                cargoTable.addCell(cargo3);


                //------------ footer

                PdfPTable footer = new PdfPTable(3);

                PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
                sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


                PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + policyObjFile.getPolicyObj().getSubtotal()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


                PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + policyObjFile.getPolicyObj().getSubtotal()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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

                PdfPCell totalAmount = new PdfPCell(new Paragraph("$" + (Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + policyObjFile.getPolicyObj().getSubtotal()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
                totalAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                totalAmount.setBorderColor(BaseColor.BLACK);


                PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
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
                document.add(footer);
                document.add(new Paragraph("\n\n\n\n"));
                document.add(headerLastValues);
                document.add(lastValues);


                document.close();

                UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);


            }
            return true;
        } catch (Exception e) {
            System.out.println("Ex " + e.getMessage() + e.getCause() + e.getStackTrace());


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


            PdfPTable table = new PdfPTable(2);

            PdfPCell cell1 = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBorderColor(BaseColor.WHITE);
            cell1.setBackgroundColor(new BaseColor(182, 208, 226));
            // add number random
            PdfPCell cell2 = new PdfPCell(new Paragraph("P ó l i z a" + "\n" + " Tipo: P" + "  Folio: ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK.darker())));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorderColor(BaseColor.WHITE);
            cell2.setBackgroundColor(new BaseColor(182, 208, 226));

            //get month of query
            PdfPCell cell3 = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderColor(BaseColor.WHITE);
            cell3.setBackgroundColor(new BaseColor(229, 231, 233));


            //param initialDate and endDate
            PdfPCell cell4 = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderColor(BaseColor.WHITE);
            cell4.setBackgroundColor(new BaseColor(229, 231, 233));


            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);


            table.setHorizontalAlignment(2);
            table.setWidthPercentage(60);


            PdfPTable headerTable = new PdfPTable(4);

            headerTable.setWidthPercentage(100);

            PdfPCell cellBody = new PdfPCell(new Paragraph("Cuenta", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellBody.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellBody.setBorderColor(BaseColor.WHITE);
            cellBody.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellName = new PdfPCell(new Paragraph("Concepto", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellName.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellName.setPaddingRight(40);
            cellName.setBorderColor(BaseColor.WHITE);
            cellName.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell cellCharge = new PdfPCell(new Paragraph("Cargo", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellCharge.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellCharge.setBorderColor(BaseColor.WHITE);
            cellCharge.setBackgroundColor(new BaseColor(182, 208, 226));

            PdfPCell cellAb = new PdfPCell(new Paragraph("Abono", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            cellAb.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellAb.setBorderColor(BaseColor.WHITE);
            cellAb.setBackgroundColor(new BaseColor(182, 208, 226));


            headerTable.addCell(cellBody);
            headerTable.addCell(cellName);
            headerTable.addCell(cellCharge);
            headerTable.addCell(cellAb);

            //System.out.println("values " + values.toString());

            PdfPTable bodyTable = new PdfPTable(4);

            bodyTable.setWidthPercentage(100);

            double totalR = 0;
            double total = 0;


            if (!policyObjFile.getPolicyObj().getRetencion_importe().isEmpty()) {
                for (int i = 0; i < policyObjFile.getPolicyObj().getRetencion_importe().size(); i++) {
                    totalR += Double.parseDouble(policyObjFile.getPolicyObj().getRetencion_importe().get(i).isEmpty() || policyObjFile.getPolicyObj().getRetencion_importe().get(i) == null ? "0" : policyObjFile.getPolicyObj().getRetencion_importe().get(i));
                }
                total = totalR + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos());

            }


            if (policyObjFile.getPolicyObj().getRetencion_importe().size() == 0) {

                //for (int i = 0; i < values.getRetencion_importe().size(); i++) {

                if ((policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN) || policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN2) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN20) || policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN22) ||
                        policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN23) || policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN24)) ||
                        policyObjFile.getPolicyObj().getRegimen().equals("606") || policyObjFile.getPolicyObj().getRegimen().equals("601")
                        && policyObjFile.getPolicyObj().getUsoCFDI().equals(CFDI) || policyObjFile.getPolicyObj().getUsoCFDI().equals(CP01.getValue())) {
                    PdfPCell accountBody = new PdfPCell();
                    PdfPCell descriptionBody = new PdfPCell();


                    //clave producto servicio


                    for (int j = 1; j < policyObjFile.getTax_id().size(); j++) {


                        accountBody = new PdfPCell(new Paragraph(policyObjFile.getTax_id().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        descriptionBody = new PdfPCell(new Paragraph(policyObjFile.getTax_description().get(j) + "\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));

                        accountBody.setBorderColorBottom(BaseColor.BLACK);
                        accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                        descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                        descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                        //PdfPCell paymentBody = new PdfPCell(new Paragraph(values.getRetencion_importe().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        PdfPCell paymentBody = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        paymentBody.setBorderColorLeft(BaseColor.WHITE);
                        paymentBody.setBorderColorRight(BaseColor.WHITE);
                        paymentBody.setBorderColorTop(BaseColor.WHITE);
                        paymentBody.setBorderColorBottom(BaseColor.BLACK);
                        paymentBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell Body = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getRetencion_importe().get(j), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                        Body.setBorderColorBottom(BaseColor.BLACK);
                        Body.setHorizontalAlignment(Element.ALIGN_CENTER);

                        bodyTable.addCell(accountBody);
                        bodyTable.addCell(descriptionBody);
                        bodyTable.addCell(paymentBody);
                        bodyTable.addCell(Body);
                    }

                } else if (policyObjFile.getPolicyObj().getRegimen().equals(REGIMEN) && policyObjFile.getPolicyObj().getUsoCFDI().equals(CFD)) {

                    PdfPCell accountBody = new PdfPCell(new Paragraph("503.01", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    accountBody.setBorderColorBottom(BaseColor.BLACK);
                    accountBody.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell descriptionBody = new PdfPCell(new Paragraph("Devoluciones, descuentos o bonificaciones sobre compras" + "\n" + policyObjFile.getCompanyName() + "\n" + "(" + policyObjFile.getPolicyObj().getTimbreFiscalDigital_UUID() + ")", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    descriptionBody.setBorderColorBottom(BaseColor.BLACK);
                    descriptionBody.setHorizontalAlignment(Element.ALIGN_CENTER);


                    //PdfPCell paymentBody = new PdfPCell(new Paragraph(values.getRetencion_importe().get(0), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
                    PdfPCell paymentBody = new PdfPCell(new Paragraph("0", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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

                // }

            }
            // ----------- cargoTable
            PdfPTable cargoTable = new PdfPTable(4);
            cargoTable.setWidthPercentage(100);

            // si es PUE es porque ya esta pago y va a las 118.01

            //si es ppd es porque va a 119.01

            PdfPCell accountCargo = new PdfPCell(new Paragraph("118.01", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            accountCargo.setBorderColorBottom(BaseColor.BLACK);
            accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountCargo.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell descripcionCargo = new PdfPCell(new Paragraph("IVA acreditable pagado", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            descripcionCargo.setBorderColorBottom(BaseColor.BLACK);
            descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
            descripcionCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cargoCargo = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cargoCargo.setBorderColorBottom(BaseColor.BLACK);
            cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cargoCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cargo = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cargo.setBorderColorBottom(BaseColor.BLACK);
            cargo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cargo.setHorizontalAlignment(Element.ALIGN_CENTER);


            cargoTable.addCell(accountCargo);
            cargoTable.addCell(descripcionCargo);
            cargoTable.addCell(cargoCargo);
            cargoTable.addCell(cargo);


            //------- abonoTable

            PdfPTable abonoTable = new PdfPTable(4);
            abonoTable.setWidthPercentage(100);

            //la forma de pago depenede el valor por ejemplo 99 va a 205.99
            //revision para cuando es el caso de tipo de cpomprobantye P

            PdfPCell accountAbono = new PdfPCell(new Paragraph("119.01", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            accountAbono.setBorderColorBottom(BaseColor.BLACK);
            accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountAbono.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell descripcionAbono = new PdfPCell(new Paragraph("IVA pendiente de pago", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            descripcionAbono.setBorderColorBottom(BaseColor.BLACK);
            descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
            descripcionAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cargoAbono = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cargoAbono.setBorderColorBottom(BaseColor.BLACK);
            cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);
            cargoAbono.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell abono = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getAmount(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            abono.setBorderColorBottom(BaseColor.BLACK);
            abono.setHorizontalAlignment(Element.ALIGN_CENTER);
            abono.setHorizontalAlignment(Element.ALIGN_CENTER);


            abonoTable.addCell(accountAbono);
            abonoTable.addCell(descripcionAbono);
            abonoTable.addCell(cargoAbono);
            abonoTable.addCell(abono);


            PdfPTable abonoT = new PdfPTable(4);
            abonoT.setWidthPercentage(100);

            //la forma de pago depenede el valor por ejemplo 99 va a 205.99
            //revision para cuando es el caso de tipo de cpomprobantye P

            PdfPCell accountAbonoT = new PdfPCell(new Paragraph("102.01", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            accountAbonoT.setBorderColorBottom(BaseColor.BLACK);
            accountAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);
            accountAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell descripcionAbonoT = new PdfPCell(new Paragraph("Bancos nacionales", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            descripcionAbonoT.setBorderColorBottom(BaseColor.BLACK);
            descripcionAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);
            descripcionAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell cargoAbonoT = new PdfPCell(new Paragraph("0.00", FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            cargoAbonoT.setBorderColorBottom(BaseColor.BLACK);
            cargoAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);
            cargoAbonoT.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell abnoT = new PdfPCell(new Paragraph(policyObjFile.getPolicyObj().getImpuestos(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            abnoT.setBorderColorBottom(BaseColor.BLACK);
            abnoT.setHorizontalAlignment(Element.ALIGN_CENTER);
            abnoT.setHorizontalAlignment(Element.ALIGN_CENTER);


            abonoT.addCell(accountAbonoT);
            abonoT.addCell(descripcionAbonoT);
            abonoT.addCell(cargoAbonoT);
            abonoT.addCell(abnoT);


            double suma = Double.parseDouble(policyObjFile.getPolicyObj().getAmount()) + Double.parseDouble(policyObjFile.getPolicyObj().getImpuestos());

            //------------ footer

            PdfPTable footer = new PdfPTable(3);

            PdfPCell sumFooter = new PdfPCell(new Paragraph("SUMAS IGUALES", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
            sumFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
            sumFooter.setBackgroundColor(new BaseColor(182, 208, 226));


            PdfPCell sumCargo = new PdfPCell(new Paragraph("$" + suma, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
            sumCargo.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell sumAbono = new PdfPCell(new Paragraph("$" + suma, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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


            PdfPCell provedor = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL, BaseColor.BLACK)));
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
            document.add(abonoT);
            document.add(footer);
            document.add(new Paragraph("\n\n\n\n"));
            document.add(headerLastValues);
            document.add(lastValues);
            document.close();


            return true;
        } catch (
                Exception e) {
            System.out.println("Ex " + e.getMessage() + e.getCause() + e.getStackTrace());
        }
        return false;

    }
}
