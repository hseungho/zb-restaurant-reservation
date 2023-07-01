package com.zerobase.hseungho.restaurantreservation.global.adapter.fileupload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUpload {

    String upload(MultipartFile file) throws IOException;

    void delete(String url);

}
