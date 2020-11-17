package com.covid.tracker.repsitory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.covid.tracker.model.Country;
import com.covid.tracker.model.CovidData;
import com.covid.tracker.model.CovidTotal;

@Repository
public interface CovidRestRepository {

	List<Country> getListOfCountriesFromAPI();

	List<CovidTotal> getCovidTotalForAllCountriesFromAPI();

	List<CovidData> getCovidDataByCountryCodeFromAPI(String code);
	List<CovidData> getCovidDataByCountryNameFromAPI(String name);

}
