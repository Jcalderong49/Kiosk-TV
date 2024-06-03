package es.KioskTV.service;

import java.util.List;
import java.util.Optional;

import es.KioskTV.entityDTO.NewDTO;

/**
 * Service interface for news operations.
 */
public interface NewService {
    /**
     * Creates a new news item.
     * 
     * @param newDTO The NewDTO object containing news details.
     * @return The created NewDTO object.
     */
    NewDTO createNews(NewDTO newDTO);

    /**
     * Retrieves a news item by its ID.
     * 
     * @param idNew The ID of the news item to retrieve.
     * @return An Optional containing the NewDTO object if found, empty otherwise.
     */
    Optional<NewDTO> getNewsById(Long idNew);

    /**
     * Retrieves all news items.
     * 
     * @return A list of NewDTO objects representing all news items.
     */
    List<NewDTO> getAllNews();

    /**
     * Updates a news item by its ID.
     * 
     * @param idNew The ID of the news item to update.
     * @param updateNew The NewDTO object containing updated news details.
     * @return The updated NewDTO object.
     */
    NewDTO updateNews(Long idNew, NewDTO updateNew);

    /**
     * Deactivates a news item by its ID.
     * 
     * @param idNew The ID of the news item to deactivate.
     */
    void deactivateNews(Long idNew);
}