package com.ddlab.rnd.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ddlab.rnd.model.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

@RestController
public class ProfileUploadController {

	@PostMapping(path = "/resume", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<String> uploadResume(
			@RequestPart("profileAsString") String profileStr,
			@RequestPart("file") MultipartFile document) {
		try {
			ObjectMapper mapper = JsonMapper.builder()
					.enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES).build();
			Profile profile = mapper.readValue(profileStr, Profile.class);
			System.out.println("Now Profile: " + profile);
			storeResume(document);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(document.getOriginalFilename());
		return new ResponseEntity<>("Your resume submitted...", HttpStatus.CREATED);
	}

	private void storeResume(MultipartFile document) {
		String dir = Paths.get("").toFile().getAbsolutePath();
		// Create a directory called resumes
		File resumeDir = new File(dir + File.separator + "resumes");
		if (!resumeDir.exists()) {
			resumeDir.mkdirs();
		}
		String fileName = document.getOriginalFilename();
		System.out.println("Uploaded Document File Name:" + fileName);
		File file = new File(resumeDir.getAbsolutePath() + File.separator + fileName);
		try (OutputStream os = new FileOutputStream(file)) {
			os.write(document.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Resume stored successfully");
	}

}
