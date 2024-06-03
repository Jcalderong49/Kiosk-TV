package es.KioskTV.service;

import java.util.List;
import java.util.Optional;

import es.KioskTV.entityDTO.LogDTO;

/**
 * Service interface for log operations.
 */
public interface LogService {
    /**
     * Creates a log record.
     * 
     * @param logDTO The LogDTO object containing log details.
     * @return The created LogDTO object.
     */
    LogDTO createLog(LogDTO logDTO);

    /**
     * Retrieves all log records.
     * 
     * @return A list of LogDTO objects representing all log records.
     */
    List<LogDTO> getAllLog();

    /**
     * Retrieves a log record by its ID.
     * 
     * @param idLog The ID of the log record to retrieve.
     * @return An Optional containing the LogDTO object if found, empty otherwise.
     */
    Optional<LogDTO> getLogById(Long idLog);

    /**
     * Updates a log record by its ID.
     * 
     * @param idLog     The ID of the log record to update.
     * @param updateLog The LogDTO object containing updated log details.
     * @return The updated LogDTO object.
     */
    LogDTO updateLog(Long idLog, LogDTO updateLog);

    /**
     * Deactivates a log record by its ID.
     * 
     * @param idLog The ID of the log record to deactivate.
     */
    void deactivateLog(Long idLog);
}