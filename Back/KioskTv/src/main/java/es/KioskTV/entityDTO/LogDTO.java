package es.KioskTV.entityDTO;

import java.util.Date;

import lombok.Data;

/**
 * Data Transfer Object for Log entity.
 */
@Data 
public class LogDTO {
    private Long id;
    private UserDTO userDTO; 
    private NewDTO newDTO; 
    private String action;
    private Date created_at;
}