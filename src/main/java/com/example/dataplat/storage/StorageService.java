package com.example.dataplat.storage;

import com.example.dataplat.entity.FileItem;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();
	Stream<FileItem> loadAllAsFileItem();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
