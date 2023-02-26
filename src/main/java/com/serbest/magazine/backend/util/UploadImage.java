package com.serbest.magazine.backend.util;


public class UploadImage {

    public static String changeNameWithTimeStamp(String filename) {
        String[] names = filename.split("\\.");
        names[0] = names[0] + System.currentTimeMillis();
        return String.join(".", names);
    }
}
