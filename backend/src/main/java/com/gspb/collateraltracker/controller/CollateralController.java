package com.gspb.collateraltracker.controller;

import com.gspb.collateraltracker.model.Collateral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/collateral")
@CrossOrigin(origins = "*")
public class CollateralController {

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

    @GetMapping
    public ResponseEntity<List<Collateral>> getAllCollateral() {
        return ResponseEntity.ok(new ArrayList<>(collateralStore.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collateral> getCollateralById(@PathVariable String id) {
        Collateral collateral = collateralStore.get(id);
        if (collateral != null) {
            return ResponseEntity.ok(collateral);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Collateral>> searchCollateral(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        
        String lowerCaseQuery = query.toLowerCase();
        List<Collateral> results = collateralStore.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Collateral> createCollateral(@RequestBody Collateral collateral) {
        collateral.setId(UUID.randomUUID().toString());
        collateral.setCreatedAt(LocalDateTime.now());
        collateral.setUpdatedAt(LocalDateTime.now());
        collateralStore.put(collateral.getId(), collateral);
        return ResponseEntity.status(HttpStatus.CREATED).body(collateral);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collateral> updateCollateral(@PathVariable String id, @RequestBody Collateral collateral) {
        Collateral existingCollateral = collateralStore.get(id);
        if (existingCollateral != null) {
            existingCollateral.setName(collateral.getName());
            existingCollateral.setDescription(collateral.getDescription());
            existingCollateral.setType(collateral.getType());
            existingCollateral.setUpdatedAt(LocalDateTime.now());
            collateralStore.put(id, existingCollateral);
            return ResponseEntity.ok(existingCollateral);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollateral(@PathVariable String id) {
        if (collateralStore.containsKey(id)) {
            collateralStore.remove(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
