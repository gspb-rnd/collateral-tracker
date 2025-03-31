package com.gspb.collateraltracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "collateralHistory")
public class CollateralHistory {
    
    @Id
    private String id;
    private String collateralId; // Reference to the original collateral item
    private String name;
    private String description;
    private String type;
    private LocalDateTime changedAt; // When this version was replaced
    
    public CollateralHistory() {
        this.changedAt = LocalDateTime.now();
    }
    
    public CollateralHistory(Collateral collateral) {
        this.collateralId = collateral.getId();
        this.name = collateral.getName();
        this.description = collateral.getDescription();
        this.type = collateral.getType();
        this.changedAt = LocalDateTime.now();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCollateralId() {
        return collateralId;
    }
    
    public void setCollateralId(String collateralId) {
        this.collateralId = collateralId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getChangedAt() {
        return changedAt;
    }
    
    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
