package es.KioskTV.entityDTO;

import es.KioskTV.entity.Role;
import lombok.Data;

/**
 * Data Transfer Object for UserUpdate entity.
 */
@Data
public class UserUpdateDTO {
    private String das;
    private String firstName;
    private String lastName;
    private Role role; 
}