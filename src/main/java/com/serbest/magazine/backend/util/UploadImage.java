package com.serbest.magazine.backend.util;


import com.serbest.magazine.backend.entity.ImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadImage {

    public static ImageModel uploadImage(MultipartFile file) throws IOException {
        if (file == null) {
            return new ImageModel();
        }
        return new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }
}
