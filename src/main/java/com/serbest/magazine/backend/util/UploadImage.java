package com.serbest.magazine.backend.util;

public class UploadImage {

    public static String changeNameWithTimeStamp(String filename) {
        if (!filename.contains(".")){
            throw new IllegalArgumentException("Please provide a valid file.");
        }
        String[] names = filename.split("\\.");

        if (names.length == 1){
            throw new IllegalArgumentException("Please provide a valid file with proper extension.");
        }

        names[0] = names[0] + System.currentTimeMillis();
        return String.join(".", names);
    }
}
