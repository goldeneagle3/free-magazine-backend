package com.serbest.magazine.backend.util;


import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadImage {

    private final Path root = Paths.get("uploads");

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    public static String changeNameWithTimeStamp(String filename) {
        String[] names = filename.split("\\.");
        names[0] = names[0] + System.currentTimeMillis();
        return String.join(".", names);
    }
}
