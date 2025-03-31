package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.CollateralHistory;
import java.util.Optional;

public interface CollateralRepositoryCustom {
    CollateralHistory saveHistory(CollateralHistory history);
    Optional<CollateralHistory> findHistoryById(String collateralId);
}
