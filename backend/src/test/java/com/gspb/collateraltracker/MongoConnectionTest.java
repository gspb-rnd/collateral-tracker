package com.gspb.collateraltracker;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.model.CollateralHistory;
import com.gspb.collateraltracker.repository.CollateralRepository;
import com.gspb.collateraltracker.repository.CollateralHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MongoConnectionTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CollateralRepository collateralRepository;
    
    @Autowired
    private CollateralHistoryRepository collateralHistoryRepository;

    @Test
    public void testMongoConnection() {
        assertNotNull(mongoTemplate);
        assertTrue(mongoTemplate.getCollectionNames().contains("collateralInfo"));
        assertTrue(mongoTemplate.getCollectionNames().contains("collateralHistory"));
    }

    @Test
    public void testCollateralRepository() {
        Collateral collateral = new Collateral();
        collateral.setName("Test Collateral Name");
        collateral.setDescription("Test description");
        collateral.setType("Test");
        
        Collateral savedCollateral = collateralRepository.save(collateral);
        
        assertNotNull(savedCollateral.getId());
        
        collateralRepository.delete(savedCollateral);
    }
    
    @Test
    public void testCollateralHistoryTracking() {
        Collateral collateral = new Collateral();
        collateral.setName("History Test Collateral");
        collateral.setDescription("Original description");
        collateral.setType("Test");
        
        Collateral savedCollateral = collateralRepository.save(collateral);
        String collateralId = savedCollateral.getId();
        
        savedCollateral.setDescription("Updated description");
        savedCollateral.setUpdatedAt(LocalDateTime.now());
        
        CollateralHistory history = new CollateralHistory(savedCollateral);
        collateralHistoryRepository.save(history);
        
        collateralRepository.save(savedCollateral);
        
        List<CollateralHistory> historyRecords = collateralHistoryRepository.findByCollateralIdOrderByChangedAtDesc(collateralId);
        assertFalse(historyRecords.isEmpty());
        assertEquals("Original description", historyRecords.get(0).getDescription());
        
        collateralRepository.delete(savedCollateral);
        collateralHistoryRepository.deleteAll(historyRecords);
    }
}
