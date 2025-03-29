package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.Collateral;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollateralRepository {
    List<Collateral> findAll();
    Optional<Collateral> findById(String id);
    Collateral save(Collateral collateral);
    void delete(Collateral collateral);
    List<Collateral> findByNameContainingIgnoreCase(String name);
}
