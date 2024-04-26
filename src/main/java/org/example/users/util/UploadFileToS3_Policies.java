package org.example.users.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.users.model.Response;

import java.io.File;
import java.util.*;

public class UploadFileToS3_Policies {
    private static String bucketName = "xmlfilesback";

    //private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all3/";
    private static String server_path = "/home/ubuntu/endpoints/eTribute-all/";

    public static Logger LOGGER = LogManager.getLogger(UploadFileToS3_Policies.class);


    public static void upload(String xml, String rfc) {

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            File file = new File(server_path + rfc + "/xml/" + xml);
            //PutObjectRequest request = new PutObjectRequest(bucketName, rfc + "/" + date + "/" + xml, file);
            PutObjectRequest request = new PutObjectRequest(bucketName, rfc + "/xml/" + xml, file);
            s3Client.putObject(request);

        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }

    public static void uploadPDF(String pdf, String rfc) {
        //System.out.println(pdf + rfc);
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            File file = new File(server_path + rfc + "/pdf/" + pdf);
            //PutObjectRequest request = new PutObjectRequest(bucketName, rfc + "/" + date + "/" + xml, file);
            PutObjectRequest request = new PutObjectRequest(bucketName, rfc + "/pdf/" + pdf, file);
            s3Client.putObject(request);

        } catch (SdkClientException e) {
            e.printStackTrace();
        }

    }


    public static Map<String, List<Response>> getFilelFromAWS(String rfc, String initial_date, String final_date) {
        List<Response> responses = new ArrayList<>();
        List<Response> ings = new ArrayList<>();
        List<Response> returned = new ArrayList<>();
        List<Response> recibidas = new ArrayList<>();
        Map<String, String> egresados = new HashMap<>();
        Map<String, String> ingresos = new HashMap<>();
        Map<String, List<Response>> facturas = new HashMap<>();
        int expirationTime = 3600;
        try {

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            ListObjectsRequest request2 = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(rfc);

            ObjectListing objectListing;
            do {
                objectListing = s3Client.listObjects(request2);
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    if (objectSummary.getKey().contains(rfc + "/xml/EGRESOS/")) {
                        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectSummary.getKey(), HttpMethod.GET);
                        request.setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000));
                        String objectUrl = s3Client.generatePresignedUrl(request).toString();
                        request.getRequestParameters();
                        String file = objectSummary.getKey().replace(rfc + "/xml/EGRESOS/", "");
                        egresados.put(file, objectUrl);

                    } else if (objectSummary.getKey().contains(rfc + "/xml/INGRESOS/")) {
                        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectSummary.getKey(), HttpMethod.GET);
                        request.setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000));
                        String objectUrl = s3Client.generatePresignedUrl(request).toString();
                        request.getRequestParameters();
                        String file = objectSummary.getKey().replace(rfc + "/xml/INGRESOS/", "");
                        ingresos.put(file, objectUrl);
                    }

                }
                request2.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());

            for (Map.Entry<String, String> egre : egresados.entrySet()) {
                Response response = ParserFile.getParseValues(egre.getValue(), rfc, "EGRESOS", egre.getKey(), initial_date, final_date);
                //response.setUrl_pdf(urls_pdf.get(i));
                responses.add(response);
            }


            for (Map.Entry<String, String> ing : ingresos.entrySet()) {
                Response response = ParserFile.getParseValues(ing.getValue(), rfc, "INGRESOS", ing.getKey(), initial_date, final_date);
                //response.setUrl_pdf(urls_pdf.get(i));
                ings.add(response);
            }

            for (Response response : responses) {
                if (response.getDescripcion() != null) {
                    returned.add(response);
                }
            }
            for (Response ingsR : ings) {
                if (ingsR.getDescripcion() != null) {
                    recibidas.add(ingsR);
                }
            }


            facturas.put("Emitidas", returned);
            facturas.put("Recibidas", recibidas);
            // FileUtils.deleteDirectory(new File(rfc));


        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
        return facturas;
    }


}
