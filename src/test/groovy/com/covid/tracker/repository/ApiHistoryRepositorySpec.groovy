package com.covid.tracker.repository

import com.covid.tracker.model.ApiHistory
import com.covid.tracker.model.Country
import com.covid.tracker.repsitory.ApiHistoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.domain.Sort
import spock.lang.Specification

@DataMongoTest
class ApiHistoryRepositorySpec extends Specification {

    @Autowired
    ApiHistoryRepository apiHistoryRepository

    def cleanup() {
        apiHistoryRepository.deleteAll()
    }

    def "save test" () {
        given:
        bootstrapDB()

        expect:
        apiHistoryRepository.count() == 2
    }

    def "findAllByApiName test" () {
        given:
        bootstrapDB()

        when:
        List<ApiHistory> apiHistoryList = apiHistoryRepository.findAllByApiName(a, Sort.Direction.ASC)

        then:
        apiHistoryList.size() == b
        apiHistoryList[0]?.type == c
        apiHistoryList[0]?.apiName == d

        where:
        a             | b | c           | d
        "testCovid1"  | 1 | "testType1" | "testCovid1"
        "testCovid12" | 0 | null        | null
    }

    def "findAllByApiNameAndType test" () {
        given:
        bootstrapDB()

        when:
        List<ApiHistory> apiHistoryList = apiHistoryRepository.findAllByApiNameAndType(a, b, Sort.Direction.ASC)

        then:
        apiHistoryList.size() == c
        apiHistoryList[0]?.type == d
        apiHistoryList[0]?.apiName == e

        where:
        a            | b           | c | d           | e
        "testCovid1" | "testType1" | 1 | "testType1" | "testCovid1"
        "testCovid1" | "test"      | 0 | null        | null
    }

    private void bootstrapDB() {
        ApiHistory apiHistory1 = new ApiHistory((String) null, "testCovid1", new Date(), "testType1")
        ApiHistory apiHistory2 = new ApiHistory((String) null, "testCovid2", new Date(), "testType2")
        apiHistoryRepository.saveAll([apiHistory1, apiHistory2])
    }
}
