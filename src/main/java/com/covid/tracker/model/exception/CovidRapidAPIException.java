package com.covid.tracker.model.exception;

import org.springframework.http.HttpStatus;

public class CovidRapidAPIException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private String error;
	private String detailedMessage;

	public CovidRapidAPIException(HttpStatus status, String error, String detailedMessage) {
		super(detailedMessage);
		this.status = status;
		this.error = error;
		this.detailedMessage = detailedMessage;

	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

}
