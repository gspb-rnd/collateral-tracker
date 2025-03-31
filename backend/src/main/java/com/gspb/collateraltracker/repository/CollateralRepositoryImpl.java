package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.CollateralHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CollateralRepositoryImpl implements CollateralRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public CollateralHistory saveHistory(CollateralHistory history) {
        return mongoTemplate.save(history, "collateralHistory");
    }
    
    @Override
    public Optional<CollateralHistory> findHistoryById(String collateralId) {
        Query query = new Query(Criteria.where("_id").is(collateralId));
        CollateralHistory history = mongoTemplate.findOne(query, CollateralHistory.class, "collateralHistory");
        return Optional.ofNullable(history);
    }
}
