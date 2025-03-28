package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.Collateral;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CollateralRepository extends MongoRepository<Collateral, String> {
    List<Collateral> findByNameContainingIgnoreCase(String name);
}
