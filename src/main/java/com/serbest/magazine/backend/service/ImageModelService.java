package com.serbest.magazine.backend.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;


public interface ImageModelService {
    void init();
    Resource load(String filename);

    void upload(InputStream inputStream,String filename) throws IOException;
}
