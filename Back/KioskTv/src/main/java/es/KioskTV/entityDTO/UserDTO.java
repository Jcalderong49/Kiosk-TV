package es.KioskTV.entityDTO;

import es.KioskTV.entity.Role;
import lombok.Data;

/**
 * Data Transfer Object for User entity.
 */
@Data 
public class UserDTO {
    private Long id;
    private String das;
    private String firstName;
    private String lastName;
    private Role role; 
}