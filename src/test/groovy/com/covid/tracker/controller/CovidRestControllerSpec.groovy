package com.covid.tracker.controller

import com.covid.tracker.model.Country
import com.covid.tracker.model.CovidTotal
import com.covid.tracker.model.exception.CovidRapidAPIException
import com.covid.tracker.service.CovidDetailsServiceImpl
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@WebMvcTest(CovidRestController)
class CovidRestControllerSpec extends Specification {

    @SpringBean
    CovidDetailsServiceImpl covidDetailsService = Mock()

    @Autowired
    MockMvc mockMvc

    def "getAllCountries returns object directly from service without modification"() {
        given:
        Country country = new Country();
        country.setName("India");
        1 * covidDetailsService.getCountries() >> [country];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/countries")).andReturn()

        then:
        results.response.contentAsString == '[{"_id":null,"name":"India","alpha2code":null,"alpha3code":null,"latitude":0.0,"longitude":0.0,"favourite":false}]'
    }

    def "getCovidDetails returns object directly from service without modification"() {
        given:
        CovidTotal covidTotal = new CovidTotal();
        covidTotal.setDeaths(10);
        1 * covidDetailsService.getTotal() >> [covidTotal];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/covidDetails")).andReturn()

        then:
        results.response.contentAsString == '[{"_id":null,"confirmed":null,"critical":null,"deaths":10,"lastChange":null,"lastUpdate":null,"recovered":null}]'
    }

    def "getCovidDataByName returns object directly from service without modification when there is no exception"() {
        given:
        CovidTotal covidTotal = new CovidTotal();
        covidTotal.setDeaths(10);
        1 * covidDetailsService.getCovidDataByName("India", false) >> [covidTotal];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/covidDetailsByName/India")).andReturn()

        then:
        results.response.contentAsString == '[{"_id":null,"confirmed":null,"critical":null,"deaths":10,"lastChange":null,"lastUpdate":null,"recovered":null}]'
    }

    def "getCovidDataByName returns responseMap when there is an exception"() {
        given:
        1 * covidDetailsService.getCovidDataByName("India", false) >> {throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", "Error message from Exception");}
        CovidTotal covidTotal = new CovidTotal();
        covidTotal.setDeaths(20);
        1 * covidDetailsService.getCovidDataByName("India", true) >> [covidTotal];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/covidDetailsByName/India")).andReturn()
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(results.response.contentAsString)

        then:
        notThrown(CovidRapidAPIException)
        jsonNode.size() == 2
        jsonNode.has("error")
        jsonNode['error'].toString() == '"Data is fetched from Database"'
        jsonNode.has("response")
        jsonNode['response'].toString() == '[{"_id":null,"confirmed":null,"critical":null,"deaths":20,"lastChange":null,"lastUpdate":null,"recovered":null}]'
    }

    def "getCovidDataByCode returns object directly from service without modification when there is no exception"() {
        given:
        CovidTotal covidTotal = new CovidTotal();
        covidTotal.setDeaths(10);
        1 * covidDetailsService.getCovidDataByCode("IN", false) >> [covidTotal];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/covidDetailsByCode/IN")).andReturn()

        then:
        results.response.contentAsString == '[{"_id":null,"confirmed":null,"critical":null,"deaths":10,"lastChange":null,"lastUpdate":null,"recovered":null}]'
    }

    def "getCovidDataByCode returns responseMap when there is an exception"() {
        given:
        1 * covidDetailsService.getCovidDataByCode("IN", false) >> {throw new CovidRapidAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Excpetion occurred while getting data from Rapid API", "Error message from Exception");}
        CovidTotal covidTotal = new CovidTotal();
        covidTotal.setDeaths(20);
        1 * covidDetailsService.getCovidDataByCode("IN", true) >> [covidTotal];

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/covidDetailsByCode/IN")).andReturn()
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(results.response.contentAsString)

        then:
        notThrown(CovidRapidAPIException)
        jsonNode.size() == 2
        jsonNode.has("error")
        jsonNode['error'].toString() == '"Data is fetched from Database"'
        jsonNode.has("response")
        jsonNode['response'].toString() == '[{"_id":null,"confirmed":null,"critical":null,"deaths":20,"lastChange":null,"lastUpdate":null,"recovered":null}]'
    }

    def "getCommentByName returns object directly from service without modification"() {
        given:
        List<String> comments = ["abc", "def"]
        1 * covidDetailsService.getCommentByName("India") >> comments;

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/commentByName/India")).andReturn()

        then:
        results.response.contentAsString == '["abc","def"]'
    }

    def "commentByCode returns object directly from service without modification"() {
        given:
        List<String> comments = ["abc", "def"]
        1 * covidDetailsService.getCommentByCode("IN") >> comments;

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/commentByCode/IN")).andReturn()

        then:
        results.response.contentAsString == '["abc","def"]'
    }

    def "getCountriesCodeMap returns object directly from service without modification"() {
        given:
        Map<String, Set<String>> responseMap = new HashMap<>();
        Set<String> stringSet = new HashSet<>();
        stringSet.add("abc")
        stringSet.add("def")
        responseMap.put("a", stringSet);
        1 * covidDetailsService.getCountriesCodeMap() >> responseMap;

        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/countriesMap")).andReturn()

        then:
        results.response.contentAsString == '{"a":["abc","def"]}'
    }

}
