package com.covid.tracker.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.covid.tracker.model.Country;
import com.covid.tracker.model.CovidData;
import com.covid.tracker.model.CovidTotal;

@Service
public interface CovidDetailsService {

	List<Country> getCountries(boolean fetchDb);

	default List<CovidTotal> getTotal(boolean fetchDb) {
		return Collections.emptyList();
	}

	List<CovidData> getCovidDataByName(String name,boolean fetchDb);

	List<CovidData> getCovidDataByCode(String code,boolean fetchDb);

	void updateCountry(Country country);

	Map<String, Set<String>> getCountriesCodeMap();

	void addCommentByName(Map<String, String> body);

	void addCommentByCode(Map<String, String> body);

	List<String> getCommentByName(String name);

	List<String> getCommentByCode(String code);
}
