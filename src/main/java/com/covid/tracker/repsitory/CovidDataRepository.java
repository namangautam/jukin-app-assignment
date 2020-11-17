package com.covid.tracker.repsitory;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.covid.tracker.model.CovidData;


public interface CovidDataRepository extends MongoRepository<CovidData, ObjectId> {

	public CovidData findByCountry(String country);
	public CovidData findByCode(String code);
	
}
