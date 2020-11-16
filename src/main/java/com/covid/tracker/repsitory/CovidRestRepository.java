package com.covid.tracker.repsitory;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.covid.tracker.model.Country;
import com.covid.tracker.model.CovidData;
import com.covid.tracker.model.CovidTotal;

@Repository
public interface CovidRestRepository {

	List<Country> getListOfCountries();

	List<CovidTotal> getTotal();

	List<CovidData> getCovidDataByCode(String code);
	List<CovidData> getCovidDataByName(String name);

}
