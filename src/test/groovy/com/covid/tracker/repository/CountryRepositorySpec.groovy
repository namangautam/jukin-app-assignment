package com.covid.tracker.repository

import com.covid.tracker.model.Country
import com.covid.tracker.repsitory.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import spock.lang.Specification

@DataMongoTest
class CountryRepositorySpec extends Specification {

    @Autowired
    CountryRepository countryRepository

    def cleanup() {
        countryRepository.deleteAll()
    }

    def "save test" () {
        given:
        bootstrapDB()

        expect:
        countryRepository.count() == 2
    }

    def "findAllByApiName test" () {
        given:
        bootstrapDB()

        when:
        Country country = countryRepository.findByName(a)

        then:
        (country != null) == b
        country?.name == c

        where:
        a             | b     | c
        "country1"    | true  | "country1"
        "testCovid12" | false | null
    }

    private void bootstrapDB() {
        Country country1 = new Country((String) null, "country1", "cn1", "cn1", 1.0, 1.0)
        Country country2 = new Country((String) null, "country2", "cn2", "cn2", 2.0, 2.0)
        countryRepository.saveAll([country1, country2])
    }

}
