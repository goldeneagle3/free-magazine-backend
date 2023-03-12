package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.service.ImageModelService;
import jakarta.servlet.ServletContext;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;


@Service
public class ImageModelServiceImpl implements ImageModelService {
    private final Path rootProd = Paths.get("./uploads");
    private final Path rootDev = Paths.get("./uploads-dev");
    private final Path rootTest = Paths.get("./uploads-test");

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    public void init() {
        try {
            File directoryDev = new File("uploads-dev");
            File directoryTest = new File("uploads-test");
            FileUtils.cleanDirectory(directoryDev);
            FileUtils.cleanDirectory(directoryTest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource load(String filename) {
        Path root = null;
        if (this.activeProfile.equals("dev")) {
            root = this.rootDev;
        } else if (this.activeProfile.equals("test")) {
            root = this.rootTest;
        } else {
            root = this.rootProd;
        }

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

    @Override
    public void upload(InputStream inputStream, String filename) throws IOException {
        Path root = null;
        if (this.activeProfile.equals("dev")) {
            root = this.rootDev;
        } else if (this.activeProfile.equals("test")) {
            root = this.rootTest;
        } else {
            root = this.rootProd;
        }

        Files.copy(inputStream, root.resolve(filename));
    }
}
