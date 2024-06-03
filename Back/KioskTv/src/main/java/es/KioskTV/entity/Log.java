package es.KioskTV.entity;

import java.util.Date;

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
import lombok.NonNull;

/**
 * Entity class representing a log entry in the system.
 */
@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    @NonNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id") 
    private News news;

    @NonNull
    private String action;

    @NonNull
    private Date created_at;

    private Date updated_at;

    private Date deleted_at;
}