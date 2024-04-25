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

    //private static String local_path = "/Users/marioalberto/IdeaProjects/eTribute-all2/";
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
        List<Response> returned = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> urls_pdf = new ArrayList<>();
        List<String> paths = new ArrayList<>();
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
                    GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectSummary.getKey(), HttpMethod.GET);
                    request.setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000));
                    String objectUrl = s3Client.generatePresignedUrl(request).toString();
                    request.getRequestParameters();

                    if (objectUrl.contains("pdf")) {
                        urls_pdf.add(objectUrl);
                    } else {
                        urls.add(objectUrl);
                    }

                    paths.add(objectSummary.getKey());
                }
                request2.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());


            File folder = new File(server_path + rfc + "/xml");
            File[] listOfFiles = folder.listFiles();
            int i = 0;
            if (!urls_pdf.isEmpty() && !urls.isEmpty()) {
                for (File file : listOfFiles) {

                    Response response = ParserFile.getParseValues(file.getPath(), initial_date, final_date);
                    response.setUrl_xml(urls.get(i));
                    response.setUrl_pdf(urls_pdf.get(i));
                    //LOGGER.info("response from Util { " + response + " }");
                    responses.add(response);
                    i++;
                }

            }
            for (Response response : responses) {
                if (response.getDescripcion() != null) {
                    returned.add(response);
                }
            }


            facturas.put("Emitidas", returned);
           // FileUtils.deleteDirectory(new File(rfc));


        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
        return facturas;
    }


}
