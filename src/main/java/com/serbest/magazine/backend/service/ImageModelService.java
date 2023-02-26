package com.serbest.magazine.backend.service;

import org.springframework.core.io.Resource;


public interface ImageModelService {
    Resource load(String filename);
}
