package com.covid.tracker.model.exception;

import org.springframework.http.HttpStatus;

public class CovidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	private Exception exception;

	private HttpStatus status;

	public CovidException(Exception e) {
		super(e);
	}

	public CovidException(String message, Exception e) {
		super(message, e);
		this.message = message;
		this.exception = e;
	}

	public CovidException(String message, Exception e, HttpStatus status) {
		super(message, e);
		this.message = message;
		this.exception = e;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
