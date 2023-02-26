package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.entity.ImageModel;
import com.serbest.magazine.backend.service.ImageModelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/photos")
public class ImageController {

    private final ImageModelService imageModelService;

    public ImageController(ImageModelService imageModelService) {
        this.imageModelService = imageModelService;
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> imageFile(@PathVariable String imageId) throws Exception {
        try {
            ImageModel imageModel = imageModelService.findById(imageId);

            return  ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageModel.getType()))
                    .body(new ByteArrayResource(imageModel.getPicByte()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws Exception {
        Resource file = imageModelService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
