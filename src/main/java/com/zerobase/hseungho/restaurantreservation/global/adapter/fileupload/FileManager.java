package com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileManager {

    /**
     * 파일 리소스를 파일 스토리지에 업로드하고 <br>
     * 업로드한 스토리지 URL 을 반환하는 메소드.
     * @param file 스토리지에 업로드하는 MultipartFile 파일
     * @return 업로드한 스토리지 URL
     * @throws IOException 파일을 byte 배열화할 때 발생할 수 있음.
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 파일 스토리지에 저장되어 있는 파일을 삭제하는 메소드.
     * @param url 파일 스토리지 URL
     */
    void delete(String url);

}
