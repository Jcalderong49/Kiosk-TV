package es.KioskTV.Exceptions;

import java.util.Date;

/**
 * Represents the response containing error details.
 */
public class ErrorDetailsResponse {
	private Date timestamp;
	private String message;
	private String details;

	/**
	 * Constructs a new ErrorDetailsResponse with the specified details.
	 *
	 * @param timestamp the timestamp of the error.
	 * @param message the error message.
	 * @param details additional details about the error.
	 */
	public ErrorDetailsResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
