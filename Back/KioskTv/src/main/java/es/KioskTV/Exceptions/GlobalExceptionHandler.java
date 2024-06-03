package es.KioskTV.Exceptions;

import java.nio.file.AccessDeniedException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import es.KioskTV.Exceptions.LogsExceptions.DuplicateLogsException;
import es.KioskTV.Exceptions.LogsExceptions.InvalidLogsDataException;
import es.KioskTV.Exceptions.LogsExceptions.LogsListNullException;
import es.KioskTV.Exceptions.LogsExceptions.LogsNotFound;
import es.KioskTV.Exceptions.NewsExceptions.DuplicateNewsException;
import es.KioskTV.Exceptions.NewsExceptions.InvalidNewsDataException;
import es.KioskTV.Exceptions.NewsExceptions.NewsListNullException;
import es.KioskTV.Exceptions.NewsExceptions.NewsNotFound;
import es.KioskTV.Exceptions.UserExceptions.DasNotFoundException;
import es.KioskTV.Exceptions.UserExceptions.DuplicateUserException;
import es.KioskTV.Exceptions.UserExceptions.InvalidUserDataException;
import es.KioskTV.Exceptions.UserExceptions.UserListNullException;
import es.KioskTV.Exceptions.UserExceptions.UserNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * #############################################
     * # GOOD_REQUEST. 200 EXCEPTION ##
     * ##############################################
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailsResponse> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.OK);
    }

    /**
     * ####################################################
     * # "User DAS not found" Exception 404 ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(DasNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleUserListEmpty(DasNotFoundException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # "User List is Empty" Exception 404 ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(UserListNullException.class)
    public ResponseEntity<ErrorDetailsResponse> handleUserListEmpty(UserListNullException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # "News List is Empty" Exception 404 ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(NewsListNullException.class)
    public ResponseEntity<ErrorDetailsResponse> handleNewsListEmpty(NewsListNullException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # "Logs List is Empty" Exception 404 ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(LogsListNullException.class)
    public ResponseEntity<ErrorDetailsResponse> handleLogsListEmpty(LogsListNullException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # Excepción 400: "User Data not valid" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorDetailsResponse> InvalidUserData(InvalidUserDataException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * ####################################################
     * # Excepción 400: "News Data not valid" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(InvalidNewsDataException.class)
    public ResponseEntity<ErrorDetailsResponse> InvalidNewsrData(InvalidNewsDataException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * ####################################################
     * # Excepción 400: "Logs Data not valid" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(InvalidLogsDataException.class)
    public ResponseEntity<ErrorDetailsResponse> InvalidLogsrData(InvalidLogsDataException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * ##########################################################
     * # Excepción 409: "Duplicate User" ##
     * ##########################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorDetailsResponse> DuplicateUser(DuplicateUserException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * ##########################################################
     * # Excepción 409: "Duplicate News" ##
     * ##########################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(DuplicateNewsException.class)
    public ResponseEntity<ErrorDetailsResponse> DuplicateNews(DuplicateNewsException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * ##########################################################
     * # Excepción 409: "Duplicate Logs" ##
     * ##########################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(DuplicateLogsException.class)
    public ResponseEntity<ErrorDetailsResponse> DuplicateLogs(DuplicateLogsException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * ####################################################
     * # Excepción 404: "UserNotFound" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorDetailsResponse> UserNotFound(UserNotFound ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # Excepción 404: "NewsNotFound" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(NewsNotFound.class)
    public ResponseEntity<ErrorDetailsResponse> NewsNotFound(NewsNotFound ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # Excepción 404: "LogsNotFound" ##
     * ####################################################
     * 
     * @param ex      The Exception That Was Thrown.
     * @param request The Current Web Request.
     * @return A ResponseEntity object that contains the details of the error.
     */
    @ExceptionHandler(LogsNotFound.class)
    public ResponseEntity<ErrorDetailsResponse> NewsLogsFound(LogsNotFound ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * ####################################################
     * # "Internal Server Error" Exception 500 ##
     * #####################################################
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                "No estás autorizado para utilizar este endpoint",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * ####################################################
     * # "Access Denied" Exception 403 ##
     * #####################################################
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsResponse> handleAccessDeniedException(AccessDeniedException ex,
            WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
                new Date(),
                "Acceso denegado",
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
}