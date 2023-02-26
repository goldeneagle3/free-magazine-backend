package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.entity.ImageModel;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.ImageModelRepository;
import com.serbest.magazine.backend.service.ImageModelService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ImageModelServiceImpl implements ImageModelService {
    private final Path root = Paths.get("./uploads");

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

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
