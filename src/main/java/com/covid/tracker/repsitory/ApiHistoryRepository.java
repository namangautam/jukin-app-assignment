package com.covid.tracker.repsitory;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.covid.tracker.model.ApiHistory;

@Repository
public interface ApiHistoryRepository extends MongoRepository<ApiHistory, ObjectId> {

	public List<ApiHistory> findAllByApiName(String apiName, Sort.Direction sort);
	public List<ApiHistory> findAllByApiNameAndType(String apiName,String type, Sort.Direction sort);

}
