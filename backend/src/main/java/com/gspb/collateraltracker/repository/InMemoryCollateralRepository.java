package com.gspb.collateraltracker.repository;

import com.gspb.collateraltracker.model.Collateral;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryCollateralRepository implements CollateralRepository {

    private static final ConcurrentMap<String, Collateral> collateralStore = new ConcurrentHashMap<>();
    
    static {
        addSampleCollateral("Sample Collateral 1", "Description for sample collateral 1", "Type A");
        addSampleCollateral("Test Collateral 2", "Description for test collateral 2", "Type B");
        addSampleCollateral("Demo Collateral 3", "Description for demo collateral 3", "Type C");
        addSampleCollateral("Example Collateral 4", "Description for example collateral 4", "Type A");
        addSampleCollateral("Collateral Item 5", "Description for collateral item 5", "Type B");
    }
    
    private static void addSampleCollateral(String name, String description, String type) {
        Collateral collateral = new Collateral();
        collateral.setId(UUID.randomUUID().toString());
        collateral.setName(name);
        collateral.setDescription(description);
        collateral.setType(type);
        collateral.setCreatedAt(LocalDateTime.now());
        collateral.setUpdatedAt(LocalDateTime.now());
        collateralStore.put(collateral.getId(), collateral);
    }

    @Override
    public List<Collateral> findAll() {
        return new ArrayList<>(collateralStore.values());
    }

    @Override
    public Optional<Collateral> findById(String id) {
        return Optional.ofNullable(collateralStore.get(id));
    }

    @Override
    public Collateral save(Collateral collateral) {
        if (collateral.getId() == null) {
            collateral.setId(UUID.randomUUID().toString());
            collateral.setCreatedAt(LocalDateTime.now());
        }
        collateral.setUpdatedAt(LocalDateTime.now());
        collateralStore.put(collateral.getId(), collateral);
        return collateral;
    }

    @Override
    public void delete(Collateral collateral) {
        collateralStore.remove(collateral.getId());
    }

    @Override
    public List<Collateral> findByNameContainingIgnoreCase(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerCaseName = name.toLowerCase();
        return collateralStore.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerCaseName))
                .collect(Collectors.toList());
    }
}
