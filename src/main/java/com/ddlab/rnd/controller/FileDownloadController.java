package com.ddlab.rnd.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadController {

	private final String BASE_PATH = "E:/sure-delete";

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		Resource resource = getFileResource(fileName);
		String headerAttachement = "attachment; filename=\"" + resource.getFilename() + "\"";
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, headerAttachement)
				.body(resource);
	}

	@GetMapping("/showfile/{fileName}")
	public ResponseEntity<Resource> showFileInBrowser(@PathVariable String fileName) {
		Resource resource = getFileResource(fileName);
		;
		MediaType fileType = fileName.endsWith("pdf") ? MediaType.APPLICATION_PDF : MediaType.IMAGE_JPEG;
		String headerInline = "inline; filename=\"" + resource.getFilename() + "\"";
		return ResponseEntity.ok().contentType(fileType)
				.header(HttpHeaders.CONTENT_DISPOSITION, headerInline)
				.body(resource);
	}

	private Resource getFileResource(String fileName) {
		Path path = Paths.get(BASE_PATH + File.separator + fileName);
		Resource resource = null;
		try {
//			resource = new InputStreamResource(new FileInputStream(new File(BASE_PATH + File.separator + fileName)));
			resource = new UrlResource(path.toUri());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resource;
	}
}
