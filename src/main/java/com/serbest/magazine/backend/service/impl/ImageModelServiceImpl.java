package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.entity.ImageModel;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.ImageModelRepository;
import com.serbest.magazine.backend.service.ImageModelService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ImageModelServiceImpl implements ImageModelService {

    private final ImageModelRepository imageModelRepository;

    public ImageModelServiceImpl(ImageModelRepository imageModelRepository) {
        this.imageModelRepository = imageModelRepository;
    }

    @Override
    public ImageModel findById(String id) {
        return imageModelRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Image", "id", id)
        );
    }
}
