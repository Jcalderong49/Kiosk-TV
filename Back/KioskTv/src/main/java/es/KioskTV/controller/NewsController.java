package es.KioskTV.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.KioskTV.Exceptions.NewsExceptions.NewsListNullException;
import es.KioskTV.Exceptions.NewsExceptions.NewsNotFound;
import es.KioskTV.Exceptions.UserExceptions.UserNotFound;
import es.KioskTV.Mapper.Mapper;
import es.KioskTV.Repository.NewsRepository;
import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.News;
import es.KioskTV.entity.Role;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;
import es.KioskTV.serviceImpl.NewsServiceImpl;
import es.KioskTV.serviceImpl.NewsWebSocketHandler;
import es.KioskTV.serviceImpl.PowerPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing news items.
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Autowired
    private PowerPointService powerPointService;

    @Autowired
    private NewsServiceImpl newService;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private NewsWebSocketHandler newsWebSocketHandler;
           

    @Autowired
    private Mapper mapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Retrieves all news items from the system.
     *
     * @return a list of NewDTO objects representing all news items
     * @throws NewsListNullException if no news items are found
     */
    @Operation(summary = "Get all news", description = "This endpoint returns a list of all news items stored in the system.")
    @GetMapping("/")
    public List<NewDTO> getAllNews() {
        List<NewDTO> news = newService.getAllNews();
        if (news == null || news.isEmpty()) {
            throw new NewsListNullException("News list is null or empty.");
        }
        return news;
    }

    /**
     * Retrieves a specific news item by its ID.
     *
     * @param id             the ID of the news item to retrieve
     * @param authentication the authentication object
     * @return the NewDTO object representing the requested news item
     * @throws NewsNotFound if the news item with the specified ID is not found
     * @throws UserNotFound if the user is not authorized to access the news item
     */
    @Operation(summary = "Get a news item by its ID", description = "This endpoint returns a specific news item based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News item found"),
            @ApiResponse(responseCode = "404", description = "News item not found")
    })
    @GetMapping("/{id}")
    public NewDTO getNewById(@PathVariable Long id, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<News> searchNews = newsRepository.findById(id);
            if (!searchNews.isPresent()) {
                throw new UserNotFound("User not found with ID: " + id);
            } else {
                News findNews = searchNews.get();
                String dasOwnNews = findNews.getUser().getDas();
                if (!userDetails.getUsername().equals(dasOwnNews)) {
                    throw new UserNotFound("Access denied. User can only access their own profile.");
                } else {
                    newService.getNewsById(id).get();
                }
            }
        }

        Optional<News> searchNews = newsRepository.findById(id);
        if (!searchNews.isPresent()) {
            throw new NewsNotFound("News not found with ID: " + id);
        }
        return newService.getNewsById(id).get();
    }

    /**
     * Updates a news item with a file.
     *
     * @param id          the ID of the news item to update
     * @param title       the title of the news item
     * @param description the description of the news item
     * @param expireDate  the expiration date of the news item
     * @param file        the file to upload
     * @param userId      the ID of the user associated with the news item
     * @return the ResponseEntity containing the updated NewDTO object
     */
    @PutMapping("/{id}/file")
    public ResponseEntity<NewDTO> updateNewWithFile(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date expireDate,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userId") Long userId) {

        Optional<News> existingNews = newsRepository.findById(id);
        if (!existingNews.isPresent()) {
            throw new NewsNotFound("News not found with ID: " + id);
        }

        try {
            String filePath = existingNews.get().getFilePath();
            if (file != null && !file.isEmpty()) {
                filePath = saveFileOnServer(file);
            }

            NewDTO newDTO = new NewDTO();
            newDTO.setTitle(title);
            newDTO.setDescription(description);
            newDTO.setExpireDate_at(expireDate);
            newDTO.setFilePath(filePath);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            newDTO.setUserDTO(userDTO);

            NewDTO updatedNews = newService.updateNews(id, newDTO);
            newsWebSocketHandler.broadcast("News updated: " + id);
            return ResponseEntity.ok(updatedNews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes a news item from the system.
     *
     * @param id             The ID of the news item to be deleted.
     * @param authentication The authentication object representing the current
     *                       user.
     * @throws UserNotFound If the user is not found or does not have permission to
     *                      delete the news item.
     * @throws NewsNotFound If the news item to be deleted is not found.
     */
    @Operation(summary = "Delete a news item", description = "This endpoint deletes an existing news item in the system.")
    @PutMapping("/delete/{id}")
    public void deleteNew(@PathVariable Long id, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROL_ADMIN.toString()))) {
            Optional<News> searchNews = newsRepository.findById(id);
            if (!searchNews.isPresent()) {
                throw new UserNotFound("User not found with ID: " + id);
            } else {
                News findNews = searchNews.get();
                String dasOwnNews = findNews.getUser().getDas();
                if (!userDetails.getUsername().equals(dasOwnNews)) {
                    throw new UserNotFound("Access denied. User can only delete their own news.");
                } else {
                    newService.deactivateNews(id);
                    newsWebSocketHandler.broadcast("delete");
                }
            }
        }

        Optional<News> searchNews = newsRepository.findById(id);
        if (!searchNews.isPresent()) {
            throw new NewsNotFound("News not found with ID: " + id);
        }
        newService.deactivateNews(id);
        newsWebSocketHandler.broadcast("delete");
    }

    /**
     * Deletes an attachment file from a news item.
     *
     * @param id The ID of the news item from which to delete the attachment.
     * @return A ResponseEntity indicating the success or failure of the operation.
     */
    @Operation(summary = "Delete an attachment from a news item", description = "This endpoint deletes the attachment file from a news item.")
    @PutMapping("/{id}/deleteAttachment")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long id) {
        Optional<News> existingNews = newsRepository.findById(id);
        if (!existingNews.isPresent()) {
            throw new NewsNotFound("News not found with ID: " + id);
        }

        try {
            News news = existingNews.get();
            String filePath = news.getFilePath();
            if (filePath != null) {
                Path fileToDelete = Paths.get(uploadDir, filePath);
                Files.deleteIfExists(fileToDelete);
                news.setFilePath(null);
                newsRepository.save(news);
                return ResponseEntity.ok("Attachment deleted successfully.");
            } else {
                return ResponseEntity.ok("No attachment found for this news item.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete attachment.");
        }
    }

    /**
     * Creates a new news item with an optional file attachment.
     *
     * @param title       The title of the news item.
     * @param description The description of the news item.
     * @param expireDate  The expiration date of the news item.
     * @param file        The optional file attachment.
     * @param userId      The ID of the user creating the news item.
     * @return The DTO representing the created news item.
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NewDTO createNewWithFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("expireDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date expireDate,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userId") Long userId) {
                NewDTO createdNews =null;
        List<String> imageUrl=new ArrayList<>();
        String originalFilename;
        if (file != null && !file.isEmpty()) {
            originalFilename = file.getOriginalFilename();

            if (originalFilename != null && (originalFilename.endsWith(".ppt") || originalFilename.endsWith(".pptx"))) {
                imageUrl=powerPointService.convertPowerPointToImages(file);
            } else {
                String filePath = saveFileOnServer(file);
            }
        }
        if (imageUrl.size()>=1) {
            for (String string : imageUrl) {
            NewDTO newDTO = new NewDTO();
            newDTO.setTitle(title);
            newDTO.setDescription(description);
            newDTO.setExpireDate_at(expireDate);
            newDTO.setFilePath(string);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            newDTO.setUserDTO(userDTO);
            createdNews = newService.createNews(newDTO);
            }
        }else{
            NewDTO newDTO = new NewDTO();
            newDTO.setTitle(title);
            newDTO.setDescription(description);
            newDTO.setExpireDate_at(expireDate);
            if (file != null && !file.isEmpty()) {
                newDTO.setFilePath(file.getOriginalFilename());
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            newDTO.setUserDTO(userDTO);
            createdNews = newService.createNews(newDTO);
        }
        newsWebSocketHandler.broadcast("create");
        return createdNews;
    }

    @GetMapping("/ws/api/news/")
    public String ping() {
        return "Server is running";
    }

    /**
     * Handles the upload of a file.
     *
     * @param file The file to be uploaded.
     * @return A ResponseEntity containing the URL of the uploaded file, or an error
     *         message if the upload fails.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = saveFileOnServer(file);
            String fileUrl = "/files/" + file.getOriginalFilename(); // Assuming you are serving files from /files URL

            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar el archivo");
        }
    }

    /**
     * Saves the file on the server.
     *
     * @param file The file to be saved.
     * @return The filename of the saved file.
     * @throws RuntimeException If an error occurs while storing the file.
     */
    private String saveFileOnServer(MultipartFile file) {
        try {
            Path directoryPath = Paths.get(uploadDir);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
    // @PutMapping("/{id}/removeImage")
    // public ResponseEntity<String> removeImageFromNews(@PathVariable Long id, Authentication authentication) {
    //     validateUserAccess(id, authentication); // Validar acceso del usuario
       
    //     Optional<News> existingNews = newsRepository.findById(id);
    //     if (!existingNews.isPresent()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("News not found with ID: " + id);
    //     }
   
    //     News news = existingNews.get();
    //     String filePath = news.getFilePath();
   
    //     if (filePath != null) {
    //         // Actualizar el campo filePath a null sin eliminar el archivo
    //         news.setFilePath(null);
    //         newsRepository.save(news);
   
    //         return ResponseEntity.ok("Image removed from news successfully and news updated.");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image to remove for news with ID: " + id);
    //     }
    // }
}
