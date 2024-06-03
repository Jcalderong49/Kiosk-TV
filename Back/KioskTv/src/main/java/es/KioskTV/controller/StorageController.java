package es.KioskTV.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.KioskTV.Exceptions.NewsExceptions.NewsNotFound;
import es.KioskTV.Repository.NewsRepository;
import es.KioskTV.entity.News;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;
import es.KioskTV.service.StorageService;
import es.KioskTV.serviceImpl.NewsServiceImpl;

/**
 * Controller for handling storage-related operations.
 */
@RestController
@RequestMapping("/api/assets")
public class StorageController {

	@Autowired
	private StorageService storageService;

	@Autowired
	private NewsServiceImpl newService;

	@Autowired
	private final NewsRepository newsRepository;

	/**
	 * Constructs a StorageController with necessary dependencies.
	 *
	 * @param storageService The storage service.
	 * @param newService     The news service.
	 * @param newsRepository The news repository.
	 */
	public StorageController(StorageService storageService, NewsServiceImpl newService, NewsRepository newsRepository) {
		super();
		this.storageService = storageService;
		this.newService = newService;
		this.newsRepository = newsRepository;
	}

	/**
	 * Retrieves an image resource.
	 *
	 * @param filename The filename of the image.
	 * @return ResponseEntity containing the image resource or an error if it cannot
	 *         be found or accessed.
	 */
	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		try {
			Path file = storageService.load(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() && resource.isReadable()) {
				String mimeType = Files.probeContentType(file);
				if (mimeType == null) {
					mimeType = "application/octet-stream";
				}

				return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().build();
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	/**
	 * Updates a news item with a file.
	 *
	 * @param id          The ID of the news item to update.
	 * @param title       The title of the news item.
	 * @param description The description of the news item.
	 * @param expireDate  The expiration date of the news item.
	 * @param file        The file to attach to the news item.
	 * @param das         The DAS of the user updating the news item.
	 * @return ResponseEntity containing the updated news item or an error if the
	 *         update fails.
	 */
	@PutMapping("/{id}/file")
	public ResponseEntity<NewDTO> updateNewWithFile(
			@PathVariable Long id,
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date expireDate,
			@RequestParam("file") MultipartFile file,
			@RequestParam("das") String das) {

		Optional<News> existingNews = newsRepository.findById(id);
		if (!existingNews.isPresent()) {
			throw new NewsNotFound("News not found with ID: " + id);
		}

		try {
			String filePath = saveFileOnServer(file);

			NewDTO newDTO = new NewDTO();
			newDTO.setTitle(title);
			newDTO.setDescription(description);
			newDTO.setExpireDate_at(expireDate);
			newDTO.setFilePath(filePath);

			UserDTO userDTO = new UserDTO();
			userDTO.setDas(das);
			newDTO.setUserDTO(userDTO);

			NewDTO updatedNews = newService.updateNews(id, newDTO);
			return ResponseEntity.ok(updatedNews);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Saves a file on the server.
	 *
	 * @param file The file to save.
	 * @return The filename of the saved file.
	 * @throws Exception If an error occurs during file storage.
	 */
	private String saveFileOnServer(MultipartFile file) throws Exception {
		return storageService.store(file);
	}
}