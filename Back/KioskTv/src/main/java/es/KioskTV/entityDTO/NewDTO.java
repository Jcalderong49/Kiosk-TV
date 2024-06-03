package es.KioskTV.entityDTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for News entity.
 */
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class NewDTO {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private Date expireDate_at;
    private UserDTO userDTO; 
}