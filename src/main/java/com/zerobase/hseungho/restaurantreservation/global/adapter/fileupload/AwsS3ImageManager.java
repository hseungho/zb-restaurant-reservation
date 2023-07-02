package com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zerobase.hseungho.restaurantreservation.global.exception.impl.BadRequestException;
import com.zerobase.hseungho.restaurantreservation.global.exception.model.ErrorCodeType;
import com.zerobase.hseungho.restaurantreservation.global.util.Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3ImageManager implements FileManager {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String DIR = "image/";
    private static final String EXTENSION = ".png";

    @Override
    public String upload(MultipartFile file) throws IOException {
        if (file == null) return null;
        return uploadImage(file);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        String fileName = DIR + Generator.generateUUID() + EXTENSION;
        s3Client.putObject(
                new PutObjectRequest(bucket, fileName, new ByteArrayInputStream(bytes), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return s3Client.getUrl(bucket, fileName).toString();
    }

    @Override
    public void delete(String url) {
        if (!StringUtils.hasText(url)) {
            return;
        }
        deleteImage(getImageKey(url));
    }

    private String getImageKey(String url) {
        if (!url.contains(DIR)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST, "deleted imageSrc is not contains image directory.");
        }
        return url.substring(url.indexOf(DIR));
    }

    private void deleteImage(String key) {
        if (s3Client.doesObjectExist(bucket, key)) {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        }
    }
}
