package com.serbest.magazine.backend;

import com.serbest.magazine.backend.service.ImageModelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	private final ImageModelService imageModelService;

	public BackendApplication(ImageModelService imageModelService) {
		this.imageModelService = imageModelService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		imageModelService.init();
	}
}
