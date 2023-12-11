package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class S3DAO {


    public static String saveImage(String image_string, String alias){
        try {
            assert alias != null;
            AmazonS3 s3 = AmazonS3ClientBuilder
                    .standard()
                    .withRegion("us-west-2")
                    .build();

            byte[] byteArray = Base64.getDecoder().decode(image_string);
            ObjectMetadata data = new ObjectMetadata();
            data.setContentLength(byteArray.length);
            data.setContentType("image/jpeg");
            PutObjectRequest request = new PutObjectRequest("ruff-tweeter-images", alias, new ByteArrayInputStream(byteArray), data).withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(request);
            String imageUrl = "https://ruff-tweeter-images.s3.us-west-2.amazonaws.com/" + alias;

            System.out.println("Result of saving image to S3: " + imageUrl);
            return imageUrl;
        } catch (Exception e) {
            System.out.println("Failed to save image to s3 with exception" + e.getMessage());
        }
        return null;
    }
}
