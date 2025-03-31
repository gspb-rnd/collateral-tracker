package com.gspb.collateraltracker.controller;

import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.model.CollateralHistory;
import com.gspb.collateraltracker.model.CollateralVersion;
import com.gspb.collateraltracker.model.Datapoint;
import com.gspb.collateraltracker.repository.CollateralRepository;
import com.gspb.collateraltracker.service.DatapointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collateral")
@CrossOrigin(origins = "*")
public class CollateralController {

    @Autowired
    private CollateralRepository collateralRepository;
    
    @Autowired
    private DatapointService datapointService;

    @GetMapping
    public ResponseEntity<List<Collateral>> getAllCollateral() {
        return ResponseEntity.ok(collateralRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collateral> getCollateralById(@PathVariable String id) {
        Optional<Collateral> collateral = collateralRepository.findById(id);
        return collateral.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Collateral>> searchCollateral(@RequestParam String query) {
        return ResponseEntity.ok(collateralRepository.findByNameContainingIgnoreCase(query));
    }

    @PostMapping
    public ResponseEntity<Collateral> createCollateral(@RequestBody Collateral collateral) {
        collateral.setId(java.util.UUID.randomUUID().toString());
        collateral.setCreatedAt(LocalDateTime.now());
        collateral.setUpdatedAt(LocalDateTime.now());
        Collateral savedCollateral = collateralRepository.save(collateral);
        
        datapointService.createDatapointsForCollateral(savedCollateral.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCollateral);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collateral> updateCollateral(@PathVariable String id, @RequestBody Collateral collateral) {
        return collateralRepository.findById(id)
                .map(existingCollateral -> {
                    CollateralHistory history = collateralRepository.findHistoryById(existingCollateral.getId())
                            .orElse(new CollateralHistory(existingCollateral.getId()));
                    
                    history.addVersion(existingCollateral);
                    
                    collateralRepository.saveHistory(history);
                    
                    existingCollateral.setName(collateral.getName());
                    existingCollateral.setDescription(collateral.getDescription());
                    existingCollateral.setType(collateral.getType());
                    existingCollateral.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(collateralRepository.save(existingCollateral));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollateral(@PathVariable String id) {
        return collateralRepository.findById(id)
                .map(collateral -> {
                    collateralRepository.delete(collateral);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/datapoints")
    public ResponseEntity<List<Datapoint>> getDatapointsForCollateral(@PathVariable String id) {
        return ResponseEntity.ok(datapointService.getDatapointsForCollateral(id));
    }
}
