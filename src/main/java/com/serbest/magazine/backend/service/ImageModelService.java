package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.entity.ImageModel;
import org.springframework.core.io.Resource;


public interface ImageModelService {

    ImageModel findById(String id);

    Resource load(String filename);
}
