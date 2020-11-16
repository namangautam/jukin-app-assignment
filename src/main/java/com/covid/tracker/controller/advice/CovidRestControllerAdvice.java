package com.covid.tracker.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.covid.tracker.model.exception.CovidException;
import com.covid.tracker.model.exception.CovidRapidAPIException;

@RestControllerAdvice
public class CovidRestControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { CovidRapidAPIException.class})
	protected ResponseEntity<Object> handleException(CovidRapidAPIException ex, WebRequest request) {
		String bodyOfResponse = ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatus(), request);
	}
	
	@ExceptionHandler(value = { CovidException.class})
	protected ResponseEntity<Object> handleException(CovidException ex, WebRequest request) {
		String bodyOfResponse = ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), ex.getStatus(), request);
	}

}
