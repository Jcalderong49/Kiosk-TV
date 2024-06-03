package es.KioskTV.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for file storage operations.
 */
public interface StorageService {
	/**
	 * Stores a file.
	 * 
	 * @param file The MultipartFile object representing the file to store.
	 * @return The filename under which the file is stored.
	 * @throws Exception if an error occurs during storage.
	 */
	public String store(MultipartFile file) throws Exception;

	/**
	 * Loads a file.
	 * 
	 * @param filename The filename of the file to load.
	 * @return The Path object representing the loaded file.
	 */
	public Path load(String filename);
}