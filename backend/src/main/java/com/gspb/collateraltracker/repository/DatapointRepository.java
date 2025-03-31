package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.Datapoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DatapointRepository extends MongoRepository<Datapoint, String> {
    List<Datapoint> findByCollateralId(String collateralId);
    List<Datapoint> findByIdStartingWith(String prefix);
}
