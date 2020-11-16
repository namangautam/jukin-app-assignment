package com.covid.tracker.model.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CovidRapidApiErrorHandler extends DefaultResponseErrorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CovidRapidApiErrorHandler.class);
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		if (response.getStatusCode().is4xxClientError()) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
				String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
				ErrorResponse errorMessage = mapper.readValue(httpBodyResponse, ErrorResponse.class);
				LOGGER.error("Error Occurred , {}",errorMessage);
				throw new CovidRapidAPIException(response.getStatusCode(), errorMessage.getError(), httpBodyResponse);
			}
		}

		if (response.getStatusCode().is5xxServerError()) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
				String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
				ErrorResponse errorMessage = mapper.readValue(httpBodyResponse, ErrorResponse.class);
				LOGGER.error("Error Occurred , {}",errorMessage);
				throw new CovidRapidAPIException(response.getStatusCode(), errorMessage.getError(), httpBodyResponse);
			}
		}
	}
}
