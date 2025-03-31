package com.gspb.collateraltracker;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.model.CollateralHistory;
import com.gspb.collateraltracker.model.CollateralVersion;
import com.gspb.collateraltracker.repository.CollateralRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MongoConnectionTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CollateralRepository collateralRepository;

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
        String uuid = UUID.randomUUID().toString();
        Collateral collateral = new Collateral();
        collateral.setId(uuid);
        collateral.setName("History Test Collateral");
        collateral.setDescription("Original description");
        collateral.setType("Test");
        collateral.setCreatedAt(LocalDateTime.now());
        collateral.setUpdatedAt(LocalDateTime.now());
        
        Collateral savedCollateral = collateralRepository.save(collateral);
        
        CollateralHistory history = new CollateralHistory(savedCollateral.getId());
        history.addVersion(savedCollateral);
        collateralRepository.saveHistory(history);
        
        savedCollateral.setDescription("First update");
        savedCollateral.setUpdatedAt(LocalDateTime.now());
        
        history.addVersion(savedCollateral);
        collateralRepository.saveHistory(history);
        
        collateralRepository.save(savedCollateral);
        
        savedCollateral.setDescription("Second update");
        savedCollateral.setUpdatedAt(LocalDateTime.now());
        
        history.addVersion(savedCollateral);
        collateralRepository.saveHistory(history);
        
        collateralRepository.save(savedCollateral);
        
        Optional<CollateralHistory> retrievedHistory = collateralRepository.findHistoryById(savedCollateral.getId());
        assertTrue(retrievedHistory.isPresent());
        assertEquals(savedCollateral.getId(), retrievedHistory.get().getId());
        assertEquals(3, retrievedHistory.get().getVersions().size());
        
        assertEquals("Original description", retrievedHistory.get().getVersions().get(0).getDescription());
        assertEquals("First update", retrievedHistory.get().getVersions().get(1).getDescription());
        assertEquals("Second update", retrievedHistory.get().getVersions().get(2).getDescription());
        
        collateralRepository.delete(savedCollateral);
        
        mongoTemplate.remove(retrievedHistory.get(), "collateralHistory");
    }
}
