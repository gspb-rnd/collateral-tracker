package com.gspb.collateraltracker;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.repository.CollateralRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

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
}
