package org.example.users.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.users.model.PolicyObjFile;

import java.io.File;
import java.io.FileOutputStream;


public class CreateFilePDFPolicy {

    private static final String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all/";
    //private static String server_path = "/home/ubuntu/endpoints/eTribute-all/";

    public CreateFilePDFPolicy() {
    }


    public static boolean makeFileEgreso(PolicyObjFile policyObjFile, String fileName, String rfc, String type) {
        try {

            File uploadDir = new File(local_path + rfc + "/pdf/" + type + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            //---------------------   Creating file  --------------------- //
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(local_path + rfc + "/pdf/" + type + "/" + fileName + ".pdf"));
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


            PdfPCell company = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
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

            /* Closing document */
            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(headerTable);
            document.close();

            /* Uploading file to S3*/
            //UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);

            return true;
        } catch (Exception e) {
            System.out.println("CreateFilePDFPolicy " + e.getMessage());
        }
        return false;

    }

    public static boolean makeFileIngreso(PolicyObjFile policyObjFile, String fileName, String rfc, String type) {
//        try {
//
//            File uploadDir = new File(local_path + rfc + "/pdf/" + type + "/");
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();
//            }
//
//            //---------------------   Creating file  --------------------- //
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, new FileOutputStream(local_path + rfc + "/pdf/" + type + "/" + fileName + ".pdf"));
//            document.open();
//
//            //---------------------   header of file  --------------------- //
//
//            PdfPTable table = new PdfPTable(2);
//
//            PdfPCell client = new PdfPCell(new Paragraph(policyObjFile.getClient(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
//            client.setHorizontalAlignment(Element.ALIGN_CENTER);
//            client.setBorderColor(BaseColor.WHITE);
//            client.setBackgroundColor(new BaseColor(182, 208, 226));
//
//
//            PdfPCell folio = new PdfPCell(new Paragraph("P ó l i z a" + "\n" + " Tipo: " + policyObjFile.getTypeOf() + "  Folio: " + policyObjFile.getFolio(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK.darker())));
//            folio.setHorizontalAlignment(Element.ALIGN_CENTER);
//            folio.setBorderColor(BaseColor.WHITE);
//            folio.setBackgroundColor(new BaseColor(182, 208, 226));
//
//
//            PdfPCell company = new PdfPCell(new Paragraph(policyObjFile.getCompanyName(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, BaseColor.BLACK)));
//            company.setHorizontalAlignment(Element.ALIGN_CENTER);
//            company.setBorderColor(BaseColor.WHITE);
//            company.setBackgroundColor(new BaseColor(229, 231, 233));
//
//
//            PdfPCell date_ = new PdfPCell(new Paragraph(policyObjFile.getDate(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
//            date_.setHorizontalAlignment(Element.ALIGN_CENTER);
//            date_.setBorderColor(BaseColor.WHITE);
//            date_.setBackgroundColor(new BaseColor(229, 231, 233));
//
//
//            table.addCell(client);
//            table.addCell(folio);
//            table.addCell(company);
//            table.addCell(date_);
//            table.setHorizontalAlignment(2);
//            table.setWidthPercentage(60);
//
//
//            PdfPTable headerTable = new PdfPTable(4);
//            headerTable.setWidthPercentage(100);
//
//            PdfPCell account = new PdfPCell(new Paragraph("Cuenta", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
//            account.setHorizontalAlignment(Element.ALIGN_CENTER);
//            account.setBorderColor(BaseColor.WHITE);
//            account.setBackgroundColor(new BaseColor(182, 208, 226));
//
//            PdfPCell concept = new PdfPCell(new Paragraph("Concepto", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
//            concept.setHorizontalAlignment(Element.ALIGN_CENTER);
//            concept.setPaddingRight(40);
//            concept.setBorderColor(BaseColor.WHITE);
//            concept.setBackgroundColor(new BaseColor(182, 208, 226));
//
//
//            PdfPCell charge = new PdfPCell(new Paragraph("Cargo", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
//            charge.setHorizontalAlignment(Element.ALIGN_CENTER);
//            charge.setBorderColor(BaseColor.WHITE);
//            charge.setBackgroundColor(new BaseColor(182, 208, 226));
//
//            PdfPCell payment = new PdfPCell(new Paragraph("Abono", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
//            payment.setHorizontalAlignment(Element.ALIGN_CENTER);
//            payment.setBorderColor(BaseColor.WHITE);
//            payment.setBackgroundColor(new BaseColor(182, 208, 226));
//
//
//            headerTable.addCell(account);
//            headerTable.addCell(concept);
//            headerTable.addCell(charge);
//            headerTable.addCell(payment);
//
//            /* Closing document */
//            document.add(table);
//            document.add(new Paragraph("\n"));
//            document.add(headerTable);
//            document.close();
//
//            /* Uploading file to S3*/
//            //UploadFileToS3_Policies.uploadPDF(fileName + ".pdf", rfc, type);
//
//            return true;
//        } catch (Exception e) {
//            System.out.println("CreateFilePDFPolicy " + e.getMessage());
//        }
        return true;
    }
}
