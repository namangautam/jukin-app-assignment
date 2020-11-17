package com.covid.tracker.service

import com.covid.tracker.model.Country
import com.covid.tracker.repsitory.CountryRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class CovidDetailsServiceImplSpec extends Specification {

    @Autowired
    CovidDetailsServiceImpl covidDetailsService

    @SpringBean
    CountryRepository countryRepository = Mock()

    def "getCountries" () {
        given:
        1 * countryRepository.findAll(*_) >> [new Country((String) null, "A", null, null, 0.0, 0.0)]

        when:
        def results = covidDetailsService.getCountries()

        then:
        results.size() == 1
    }


}
