package com.covid.tracker.repository

import com.covid.tracker.model.Country
import com.covid.tracker.model.CovidData
import com.covid.tracker.model.CovidTotal
import com.covid.tracker.repsitory.CovidRestRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest
class CovidRestRepositoryImplSpec extends Specification {

    @Autowired
    CovidRestRepositoryImpl covidRestRepository

    def "getListOfCountriesFromAPI 3rd part API test" () {
        when:
        def result = covidRestRepository.getListOfCountriesFromAPI()

        then:
        result.size() > 0
        result[0] instanceof Country
        result.sort({ it.name })[0].name == "Afghanistan"
    }

    def "getCovidTotalForAllCountriesFromAPI 3rd part API test" () {
        when:
        // This is needed to avoid - "You have exceeded the rate limit per second for your plan, BASIC, by the API provider"
        Thread.sleep(2000)
        def result = covidRestRepository.getCovidTotalForAllCountriesFromAPI()

        then:
        result.size() == 1
        result[0] instanceof CovidTotal
    }

    def "getCovidDataByCountryNameFromAPI 3rd part API test" () {
        when:
        Thread.sleep(2000)
        def result = covidRestRepository.getCovidDataByCountryNameFromAPI("India")

        then:
        result.size() == 1
        result[0] instanceof CovidData
        result[0].country == "India"
        result[0].code == "IN"
    }

    def "getCovidDataByCountryCodeFromAPI 3rd part API test" () {
        when:
        Thread.sleep(2000)
        def result = covidRestRepository.getCovidDataByCountryCodeFromAPI("IN")

        then:
        result.size() == 1
        result[0] instanceof CovidData
        result[0].country == "India"
        result[0].code == "IN"
    }


}
