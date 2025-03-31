package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.model.CollateralHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollateralRepository extends MongoRepository<Collateral, String>, CollateralRepositoryCustom {
    List<Collateral> findByNameContainingIgnoreCase(String name);
}
