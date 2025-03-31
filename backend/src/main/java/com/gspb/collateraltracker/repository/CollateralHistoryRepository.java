package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.CollateralHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CollateralHistoryRepository extends MongoRepository<CollateralHistory, String> {
    List<CollateralHistory> findByCollateralIdOrderByChangedAtDesc(String collateralId);
}
