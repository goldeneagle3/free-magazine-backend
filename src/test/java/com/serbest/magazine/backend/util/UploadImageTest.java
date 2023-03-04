package com.serbest.magazine.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UploadImageTest {

    @Test
    public void changeNameWithTimeStamp_filenameWithoutDot() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UploadImage.changeNameWithTimeStamp("filename"));
    }

    @Test
    public void changeNameWithTimeStamp_filenameWithoutExtension() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UploadImage.changeNameWithTimeStamp("filename."));
    }

    @Test
    public void changeNameWithTimeStamp_successStatus(){
        String filename = "image.png";
        String[] names = filename.split("\\.");
        names[0] = names[0] + System.currentTimeMillis();
        String res = String.join(".",names);
        assertEquals(res.length(),UploadImage.changeNameWithTimeStamp(filename).length());
        assertFalse(UploadImage.changeNameWithTimeStamp(filename).isBlank());
        assertFalse(UploadImage.changeNameWithTimeStamp(filename).isEmpty());
    }
}