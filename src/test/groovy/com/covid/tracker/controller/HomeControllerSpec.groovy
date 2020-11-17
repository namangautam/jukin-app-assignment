package com.covid.tracker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

@WebMvcTest(controllers = [HomeController])
class HomeControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def "home template returns correct page"() {
        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/home")).andReturn()

        then:
        results.modelAndView.viewName == "homePage"
    }

    def "getCountry template returns correct page"() {
        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/country")).andReturn()

        then:
        results.modelAndView.viewName == "country"
    }

    def "countryByName template returns correct page"() {
        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/countryByName")).andReturn()

        then:
        results.modelAndView.viewName == "countryByName"
    }

    def "countryByCode template returns correct page"() {
        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/countryByCode")).andReturn()

        then:
        results.modelAndView.viewName == "countryByCode"
    }

    def "dataPage template returns correct page"() {
        when:
        def results = mockMvc.perform(MockMvcRequestBuilders.get("/dataPage")).andReturn()

        then:
        results.modelAndView.viewName == "data"
    }

}
