package com.covid.tracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.covid.tracker.model.ApiHistory;
import com.covid.tracker.model.Country;
import com.covid.tracker.model.CovidData;
import com.covid.tracker.model.CovidTotal;
import com.covid.tracker.model.exception.CovidException;
import com.covid.tracker.model.exception.CovidRapidAPIException;
import com.covid.tracker.repsitory.ApiHistoryRepository;
import com.covid.tracker.repsitory.CountryRepository;
import com.covid.tracker.repsitory.CovidDataRepository;
import com.covid.tracker.repsitory.CovidRestRepository;
import com.covid.tracker.repsitory.TotalRepository;

@Service
public class CovidDetailsServiceImpl implements CovidDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CovidDetailsServiceImpl.class);
	
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private TotalRepository totalRepository;

	@Autowired
	private CovidRestRepository covidRestRepository;

	@Autowired
	private CovidDataRepository covidDataRepository;

	@Autowired
	private ApiHistoryRepository apiHistoryRepository;

	@Override
	@Transactional
	public List<Country> getCountries(boolean fetchDb) {
		// add logic update it after 7 days
		boolean isOutdated = checkOutDated("countries");
		List<Country> countries = new ArrayList<>();
		List<Country> countriesInDB = new ArrayList<>();
		try {
			if(fetchDb) {
				return countryRepository.findAll();
			}
			if (isOutdated) {
				countries = covidRestRepository.getListOfCountries();
				countriesInDB = countryRepository.findAll();
				List<String> countriesName = countriesInDB.stream().map(Country::getName).collect(Collectors.toList());

				countriesInDB.addAll(countries.stream().filter(o -> !countriesName.contains(o.getName()))
						.collect(Collectors.toList()));
				updateHistory("countries");
				countryRepository.saveAll(countriesInDB);
				isOutdated = false;

			} else {
				countriesInDB = countryRepository.findAll();
			}

		} catch (CovidRapidAPIException e) {
			throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", e.getDetailedMessage());
		} catch (Exception e) {
			throw new CovidException(String.format("Excpetion occurred while updating get countries"), e);
		}

		return countriesInDB;
	}

	@Override
	@Transactional
	public List<CovidTotal> getTotal(boolean fetchDb) {
		// add logic update it after 7 days
		boolean isOutdated = checkOutDated("total");
		List<CovidTotal> covidDetails = new ArrayList<>();
		List<CovidTotal> covidDetailsInDB = new ArrayList<>();

		try {
			if(fetchDb) {
				return totalRepository.findAll();
			}
			if (isOutdated) {
				covidDetails = covidRestRepository.getTotal();
				final CovidTotal totals = covidDetails.get(0);
				covidDetailsInDB = totalRepository.findAll();
				// There will always be one record
				if(CollectionUtils.isEmpty(covidDetailsInDB)) {
					covidDetailsInDB.addAll(covidDetails);
				}else {
				covidDetailsInDB.forEach(y -> {
					y.setConfirmed(totals.getConfirmed());
					y.setCritical(totals.getCritical());
					y.setDeaths(totals.getDeaths());
					y.setRecovered(totals.getRecovered());
					y.setLastChange(totals.getLastChange());
					y.setLastUpdate(totals.getLastUpdate());

				});
				}
				updateHistory("total");
				totalRepository.saveAll(covidDetailsInDB);
				isOutdated = false;

			} else {
				covidDetailsInDB = totalRepository.findAll();
			}
		} catch (CovidRapidAPIException e) {
			throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", e.getDetailedMessage());
		} catch (Exception e) {
			throw new CovidException(String.format("Excpetion occurred while updating total covid data"), e);
		}

		return covidDetailsInDB;
	}

	private void updateHistory(String apiName) {
		try {
			List<ApiHistory> history = apiHistoryRepository.findAllByApiName(apiName, Sort.Direction.DESC);
			if (CollectionUtils.isEmpty(history)) {
				apiHistoryRepository.save(new ApiHistory(new ObjectId(), apiName, new Date(), null));
			} else {
				ApiHistory hist = history.get(0);
				hist.setDate(new Date());
				apiHistoryRepository.save(hist);
			}
		} catch (Exception e) {
			throw new CovidException(
					String.format("Excpetion occurred while updating history with apiName {}", apiName), e);
		}

	}

	private void updateHistory(String apiName, String type) {
		try {
			List<ApiHistory> history = apiHistoryRepository.findAllByApiNameAndType(apiName, type, Sort.Direction.DESC);
			if (CollectionUtils.isEmpty(history)) {
				apiHistoryRepository.save(new ApiHistory(new ObjectId(), apiName, new Date(), type));
			} else {
				ApiHistory hist = history.get(0);
				hist.setDate(new Date());
				apiHistoryRepository.save(hist);
			}
		} catch (Exception e) {
			throw new CovidException(String
					.format("Excpetion occurred while updating history with apiName {} and type {}", apiName, type), e);
		}

	}

	@Override
	@Transactional
	public List<CovidData> getCovidDataByName(String name,boolean fetchDb) {
		boolean isOutdated = checkOutDated("name", name);
		CovidData covidDataInDB = null;
		List<CovidData> covidData = null;
		try {
			if(fetchDb) {
				return Arrays.asList(covidDataRepository.findByCountry(name));
			}
			if (isOutdated) {
				covidDataInDB = covidDataRepository.findByCountry(name);
				covidData = covidRestRepository.getCovidDataByName(name);
				if (!CollectionUtils.isEmpty(covidData)) {
					covidDataInDB = CovidData.copy(covidData.get(0), covidDataInDB);

				}
				updateHistory("name", name);
				covidDataRepository.save(covidDataInDB);
				isOutdated = false;
			} else {
				covidDataInDB = covidDataRepository.findByCountry(name);

			}
		} catch (CovidRapidAPIException e) {
			throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", e.getDetailedMessage());
		} catch (Exception e) {
			throw new CovidException(
					String.format("Exception occurred while getting covid data by country name %s", name), e);
		}
		return Arrays.asList(covidDataInDB);
	}

	@Override
	@Transactional
	public List<CovidData> getCovidDataByCode(String code,boolean fetchDb) {
		boolean isOutdated = checkOutDated("code", code);
		CovidData covidDataInDB = null;
		CovidData covidData = null;
		try {
			if(fetchDb) {
				return Arrays.asList(covidDataRepository.findByCode(code));
			}
			if (isOutdated) {
				covidDataInDB = covidDataRepository.findByCode(code);
				List<CovidData> covidDataFromService = covidRestRepository.getCovidDataByCode(code);
				// covidDataInDB = CovidData.copy(covidData, covidDataInDB);
				if (CollectionUtils.isEmpty(covidDataFromService)) {
					// throw exception from here or message
				}
				covidData = covidDataFromService.get(0);
				covidDataInDB=CovidData.copy(covidData, covidDataInDB);

				updateHistory("code", code);
				covidDataRepository.save(covidDataInDB);
				isOutdated = false;
			} else {
				covidDataInDB = covidDataRepository.findByCode(code);

			}

		} catch (CovidRapidAPIException e) {
			throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", e.getDetailedMessage());
		} catch (Exception e) {
			throw new CovidException(
					String.format("Exception occurred while getting covid data by country code %s", code), e);
		}
		return Arrays.asList(covidDataInDB);
	}

	@Override
	@Transactional
	public void updateCountry(Country country) {
		try {
			Country dbCountry = countryRepository.findByName(country.getName());
			if (null != dbCountry) {
				country.set_id(dbCountry.get_id());
				dbCountry = country;
			}
			countryRepository.save(dbCountry);
		} catch (Exception e) {
			throw new CovidException(
					String.format("Exception occurred while updating country with name %s", country.getName()), e);
		}
	}

	private boolean checkOutDated(String apiName) {
		List<ApiHistory> result = apiHistoryRepository.findAllByApiName(apiName, Sort.Direction.DESC);
		return CollectionUtils.isEmpty(result) ? true : getDiff(result.get(0).getDate()) > 7 ? true : false;
	}

	private boolean checkOutDated(String apiName, String type) {
		List<ApiHistory> result = apiHistoryRepository.findAllByApiNameAndType(apiName, type, Sort.Direction.DESC);
		return CollectionUtils.isEmpty(result) ? true : getDiff(result.get(0).getDate()) > 7 ? true : false;
	}

	private long getDiff(Date date) {
		Date currDate = new Date();
		if (date == null)
			return 8;
		return TimeUnit.MILLISECONDS.toDays(currDate.getTime() - date.getTime()) % 365;
	}

	@Override
	public Map<String, Set<String>> getCountriesCodeMap() {
		try {
			List<Country> countries = countryRepository.findAll();
			if (CollectionUtils.isEmpty(countries)) {
				// call countries services
				countries = getCountries(false);
				if (CollectionUtils.isEmpty(countries)) {
					return Collections.emptyMap();
				}
				countryRepository.saveAll(countries);
			}

			Map<String, Set<String>> countriesCodeMap = new HashMap<>();
			for (Country country : countries) {
				updateMapIfKeyPresent("countries", countriesCodeMap, country.getName());
				updateMapIfKeyPresent("code", countriesCodeMap, country.getAlpha2code());
				updateMapIfKeyNotPresent("code", countriesCodeMap, country.getAlpha2code());
				updateMapIfKeyNotPresent("countries", countriesCodeMap, country.getName());
			}
			return countriesCodeMap;
		} catch (Exception e) {
			throw new CovidException(String.format("Error Occurred while executing query please check"), e);
		}
	}

	private void updateMapIfKeyNotPresent(String key, Map<String, Set<String>> countriesCodeMap, String value) {
		countriesCodeMap.computeIfAbsent(key, (a) -> {
			Set<String> values = new HashSet<>();
			values.add(value);
			return values;
		});
	}

	private void updateMapIfKeyPresent(String key, Map<String, Set<String>> countriesCodeMap, String value) {
		countriesCodeMap.computeIfPresent(key, (a, b) -> {
			b.add(value);
			return b;
		});
	}

	@Override
	public void addCommentByName(Map<String, String> body) {
		try {
			CovidData result = covidDataRepository.findByCountry(body.get("name"));
			if (null == result) {
				throw new CovidException(
						String.format("Comment with specified country name %s does not exist", body.get("name")),
						new NullPointerException());
			}

			result.getComments().add(body.get("comment"));
			covidDataRepository.save(result);
		} catch (Exception e) {
			throw new CovidException(String.format("Error while saving comment by country name %s", body.get("name")),
					e);
		}

	}

	@Override
	public void addCommentByCode(Map<String, String> body) {
		try {
			CovidData result = covidDataRepository.findByCode(body.get("code"));
			if (null == result) {
				throw new CovidException(
						String.format("Comment with specified country code %s does not exist", body.get("code")),
						new NullPointerException());
			}

			result.getComments().add(body.get("comment"));
			covidDataRepository.save(result);
		} catch (Exception e) {
			throw new CovidException(String.format("Error while saving comment by country code %s", body.get("code")),
					e);
		}

	}

	@Override
	public List<String> getCommentByName(String name) {
		try {
			CovidData result = covidDataRepository.findByCountry(name);
			if (null == result) {
				throw new CovidException(String.format("Comment with specified country code %s does not exist", name),
						new Exception());
			}

			return result.getComments();
		} catch (Exception e) {
			throw new CovidException(String.format("Exception Occurred white executing findby country name ", name), e);
		}
	}

	@Override
	public List<String> getCommentByCode(String code) {
		try {
			CovidData result = covidDataRepository.findByCode(code);
			if (null == result) {
				throw new CovidException(String.format("Comment with specified country code %s does not exist", code),
						new Exception());
			}
			return result.getComments();
		} catch (Exception e) {
			throw new CovidException(String.format("Exception Occurred white executing findby country code ", code), e);
		}
	}

}
