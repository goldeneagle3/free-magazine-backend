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

    public static String changeNameWithTimeStamp(String filename) {
        String[] names = filename.split("\\.");
        names[0] = names[0] + System.currentTimeMillis();
        return String.join(".", names);
    }
}
