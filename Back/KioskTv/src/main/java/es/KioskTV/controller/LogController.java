package es.KioskTV.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.KioskTV.Exceptions.LogsExceptions.DuplicateLogsException;
import es.KioskTV.Exceptions.LogsExceptions.InvalidLogsDataException;
import es.KioskTV.Exceptions.LogsExceptions.LogsListNullException;
import es.KioskTV.entityDTO.LogDTO;
import es.KioskTV.serviceImpl.LogServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing log records.
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogServiceImpl logService;

    /**
     * Retrieves all log records from the system.
     *
     * @return a list of LogDTO objects representing all log records
     * @throws LogsListNullException if no log records are found
     */
    @Operation(summary = "Get all log records", description = "This endpoint returns a list of all log records stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "404", description = "No log records found")
    })
    @GetMapping("/")
    public List<LogDTO> getAllLogs() {
        List<LogDTO> logs = logService.getAllLog();
        if (logs == null || logs.isEmpty()) {
            throw new LogsListNullException("No log records found.");
        }
        return logs;
    }

    /**
     * Creates a new log record in the system.
     *
     * @param logDTO the LogDTO object representing the log record to be created
     * @return the created LogDTO object
     * @throws DuplicateLogsException if the log record already exists
     * @throws InvalidLogsDataException if the log data is invalid
     */
    @Operation(summary = "Create a new log record", description = "This endpoint creates a new log record in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created log record"),
            @ApiResponse(responseCode = "400", description = "Invalid log data"),
            @ApiResponse(responseCode = "409", description = "Log record already exists")
    })
    @PostMapping
    public LogDTO createLog(@RequestBody LogDTO logDTO) {
        try {
            return logService.createLog(logDTO);
        } catch (DuplicateLogsException e) {
            throw new DuplicateLogsException("Log record already exists.");
        } catch (InvalidLogsDataException e) {
            throw new InvalidLogsDataException("Invalid log data.");
        }
    }
}