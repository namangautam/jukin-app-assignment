package com.covid.tracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.covid.tracker.service.CovidDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.covid.tracker.model.Country;
import com.covid.tracker.model.exception.CovidRapidAPIException;
import com.covid.tracker.service.CovidDetailsService;

@RestController
public class CovidRestController {

	@Autowired
	private CovidDetailsServiceImpl covidDetailsService;

	@GetMapping(value = { "/countries" })
	public Object getAllCountries() {
		try {
			return covidDetailsService.getCountries(false);
		} catch (CovidRapidAPIException e) {
			Object responseMap = new HashMap<>();
			Object country = covidDetailsService.getCountries(true);
			((HashMap<Object, Object>) responseMap).put("error", "Data is fetched from Database");
			((HashMap) responseMap).put("response", country);
			return responseMap;
		}
	}

	@GetMapping(value = { "/covidDetails" })
	public Object getCovidDetails() {
		try {
			return covidDetailsService.getTotal(false);
		} catch (CovidRapidAPIException e) {
			Object responseMap = new HashMap<>();
			Object covidTotal = covidDetailsService.getTotal(true);
			((HashMap) responseMap).put("error", "Data is fetched from Database");
			((HashMap) responseMap).put("response", covidTotal);
			return responseMap;
		}

	}

	@GetMapping(value = { "/covidDetailsByName/{name}" })
	public Object getCovidDataByName(@PathVariable("name") String name) {
		try {
			return covidDetailsService.getCovidDataByName(name, false);
		} catch (CovidRapidAPIException e) {
			Object responseMap = new HashMap<>();
			Object covidDataByName = covidDetailsService.getCovidDataByName(name, true);
			((HashMap) responseMap).put("error", "Data is fetched from Database");
			((HashMap) responseMap).put("response", covidDataByName);
			return responseMap;
		}

	}

	@GetMapping(value = { "/covidDetailsByCode/{code}" })
	public Object getCovidDataByCode(@PathVariable("code") String code) {
		try {
			return covidDetailsService.getCovidDataByCode(code, false);
		} catch (CovidRapidAPIException e) {
			Object responseMap = new HashMap<>();
			Object covidDataByCode = covidDetailsService.getCovidDataByCode(code, true);
			((HashMap) responseMap).put("error", "Data is fetched from Database");
			((HashMap) responseMap).put("response", covidDataByCode);
			return responseMap;
		}

	}

	@PostMapping(value = { "/updateCountry" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void getUpdatedCountry(@RequestBody Country country) {
		covidDetailsService.updateCountry(country);
	}

	@PostMapping(value = { "/addCommentsByName" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void addCommentByName(@RequestBody Map<String, String> body) {
		covidDetailsService.addCommentByName(body);
	}

	@PostMapping(value = { "/addCommentsByCode" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void addCommentByCode(@RequestBody Map<String, String> body) {
		covidDetailsService.addCommentByCode(body);
	}

	@GetMapping(value = { "/commentByName/{name}" })
	public List<String> getCommentByName(@PathVariable String name) {
		return covidDetailsService.getCommentByName(name);
	}

	@GetMapping(value = { "/commentByCode/{code}" })
	public List<String> getCommentByCode(@PathVariable String code) {
		return covidDetailsService.getCommentByCode(code);
	}

	@GetMapping(value = { "/countriesMap" })
	public Map<String, Set<String>> getCountriesCodeMap() {
		return covidDetailsService.getCountriesCodeMap();
	}
}
