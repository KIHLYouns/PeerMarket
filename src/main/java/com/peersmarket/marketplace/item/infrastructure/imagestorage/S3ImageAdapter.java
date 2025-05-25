package com.peersmarket.marketplace.item.infrastructure.imagestorage;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.peersmarket.marketplace.item.application.model.ImageFileWrapper;
import com.peersmarket.marketplace.item.application.port.out.ImageStoragePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ImageAdapter implements ImageStoragePort {

    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    @Override
    public String storeImage(ImageFileWrapper imageFileWrapper) {
        String originalFilename = imageFileWrapper.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String key = imageFileWrapper.getKeyPrefix() + UUID.randomUUID().toString() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(imageFileWrapper.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ) // Pour rendre l'image publiquement accessible
                .build();

        try {
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(imageFileWrapper.getInputStream(), imageFileWrapper.getSize()));
            URL imageUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key));
            log.info("Image uploaded to S3. Key: {}, URL: {}", key, imageUrl.toString());
            return imageUrl.toString();
        } catch (S3Exception e) {
            log.error("Error uploading image to S3. Key: {}. Error: {}", key, e.getMessage(), e);
            throw new RuntimeException("Failed to store image in S3: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            log.warn("Attempted to delete image with null or blank URL.");
            return;
        }
        try {
            String key = extractKeyFromS3Url(imageUrl);
            if (key == null) {
                log.error("Could not extract key from S3 URL: {}", imageUrl);
                throw new IllegalArgumentException("Invalid S3 URL format for deletion.");
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Image deleted from S3. Key: {}", key);
        } catch (S3Exception e) {
            log.error("Error deleting image from S3. URL: {}. Error: {}", imageUrl, e.getMessage(), e);
            throw new RuntimeException("Failed to delete image from S3: " + e.getMessage(), e);
        }
    }

    private String extractKeyFromS3Url(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String path = url.getPath();
            if (path.startsWith("/")) {
                return path.substring(1);
            }
            return path;
        } catch (Exception e) {
            log.error("Failed to parse S3 URL to extract key: {}", imageUrl, e);
            return null;
        }
    }
}
