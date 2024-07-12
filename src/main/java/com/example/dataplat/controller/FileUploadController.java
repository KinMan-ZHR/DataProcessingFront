package com.example.dataplat.controller;
import com.example.dataplat.entity.FileItem;
import com.example.dataplat.storage.StorageFileNotFoundException;
import com.example.dataplat.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
//@RequestMapping("/upload")
import org.springframework.http.MediaType;

@RestController // 使用 @RestController 注解，表明这是一个 REST 控制器
//@RequestMapping("/api") // 定义一个基础路径前缀
public class FileUploadController {

	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	// GET /files: 返回文件列表的 JSON 格式
	@GetMapping("/files")
	public ResponseEntity<List<FileItem>> serveFileList() {
//		List<String> fileUrls = storageService.loadAll().map(
//						path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//								"serveFile", path.getFileName().toString()).build().toUri().toString())
//				.collect(Collectors.toList());
//		log.info("File URLs: {}", fileUrls);
		List<FileItem> fileItems = storageService.loadAll().map(
				path -> new FileItem(path.getFileName().toString(),
						MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
								"serveFile", path.getFileName().toString()).build().toUri().toString()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(fileItems);
	}

	// GET /files/{filename}: 返回文件资源
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		if (file == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	// POST /files: 处理文件上传
	@PostMapping("/files")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		log.info("Uploaded file: {}", file.getOriginalFilename());
		storageService.store(file);
		return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
	}

	// 异常处理器
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}


