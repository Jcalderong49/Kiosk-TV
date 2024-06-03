package es.KioskTV.entity;

import java.util.Date;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing news entry in the system.
 */
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    private String title;

   
    private String description;

    private String filePath;

    @NonNull
    private Date created_at;

    @NonNull
    private Date expireDate_at;

    private Date updated_at;

    private Date deleted_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;
}