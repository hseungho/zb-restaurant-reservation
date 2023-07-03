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

/**
 * AwsS3ImageManager 는 FileManager 인터페이스를 구현체한 클래스로, <br>
 * AWS S3 솔루션에 접근하여 이미지 파일을 <br>
 * 업로드하고 삭제하는 컴포넌트 클래스다.
 */
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

    /**
     * MultipartFile 을 byte 배열로 변환시키고, <br>
     * ObjectMetadata 객체에 담아 AWS S3에 업로드하는 메소드. <br>
     * <br>
     * 저장되는 이미지의 디렉토리는 상수 DIR 이며, 확장자는 상수 EXTENSION 으로 지정한다.
     * @param file 업로드할 MultipartFile 인터페이스
     * @return 업로드한 스토리지 URL
     * @throws IOException 파일을 byte 배열화할 떄 발생할 수 있음.
     */
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

    /**
     * 파일 스토리지 URL 에서 스토리지 주소의 호스트 주소를 제거하고, <br>
     * 스토리지 URL 중 디렉토리와 파일이름만 추출하는 메소드.
     * @param url 파일 스토리지 URL
     * @return 디렉토리와 파일이름이 담긴 문자열
     */
    private String getImageKey(String url) {
        if (!url.contains(DIR)) {
            throw new BadRequestException(ErrorCodeType.BAD_REQUEST, "deleted imageSrc is not contains image directory.");
        }
        return url.substring(url.indexOf(DIR));
    }

    /**
     * 디렉토리와 파일이름을 이용하여 AWS S3에서 해당 파일을 삭제하는 메소드. <br>
     * <br>
     * 먼저 해당 파일이름을 가지는 S3 객체가 있는지 확인하고, <br>
     * AWS S3에 해당 S3 객체가 존재할 경우, 삭제함.
     * @param key 파일이름인 객체 키
     */
    private void deleteImage(String key) {
        if (s3Client.doesObjectExist(bucket, key)) {
            s3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        }
    }
}
