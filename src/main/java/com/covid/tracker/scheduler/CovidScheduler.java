package com.covid.tracker.scheduler;

import com.covid.tracker.model.ApiHistory;
import com.covid.tracker.model.Country;
import com.covid.tracker.model.CovidTotal;
import com.covid.tracker.model.exception.CovidException;
import com.covid.tracker.repsitory.ApiHistoryRepository;
import com.covid.tracker.repsitory.CountryRepository;
import com.covid.tracker.repsitory.CovidRestRepository;
import com.covid.tracker.repsitory.TotalRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CovidScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CovidScheduler.class);

    @Autowired
    private CovidRestRepository covidRestRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ApiHistoryRepository apiHistoryRepository;

    @Autowired
    private TotalRepository totalRepository;

    private void updateOutdatedCountries() {
        try {
            LOGGER.info("updateOutdatedCountries: Starting scheduler to update ");
            List<Country> countries = covidRestRepository.getListOfCountriesFromAPI();
            List<Country> countriesInDB = countryRepository.findAll();
            List<String> countriesName = countriesInDB.stream().map(Country::getName).collect(Collectors.toList());

            countriesInDB.addAll(countries.stream().filter(o -> !countriesName.contains(o.getName()))
                    .collect(Collectors.toList()));
            updateHistory("countries");
            countryRepository.saveAll(countriesInDB);
            LOGGER.info("updateOutdatedCountries: finish ");
        } catch (Exception e) {
            LOGGER.error("updateOutdatedCountries: Error - ", e);
        }
    }

    private void updateOutdatedCovidDetails() {
        try {
            LOGGER.info("updateOutdatedCovidDetails: Starting scheduler to update ");
            List<CovidTotal> covidDetails = covidRestRepository.getCovidTotalForAllCountriesFromAPI();
            final CovidTotal totals = covidDetails.get(0);
            List<CovidTotal> covidDetailsInDB = totalRepository.findAll();
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
            LOGGER.info("updateOutdatedCovidDetails: finish ");
        } catch (Exception e) {
            LOGGER.error("updateOutdatedCovidDetails: Error - ", e);
        }
    }

    @Scheduled(cron = "${update.cron}")
    @Transactional
    @PostConstruct
    public void runAtStart() {
        updateOutdatedCountries();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("exception in thread sleep", e);
        }
        updateOutdatedCovidDetails();
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
}
