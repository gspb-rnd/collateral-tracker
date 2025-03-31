package com.gspb.collateraltracker.model;

import java.time.LocalDateTime;

public class CollateralVersion {
    private String name;
    private String description;
    private String type;
    private LocalDateTime updatedAt;
    
    public CollateralVersion() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public CollateralVersion(Collateral collateral) {
        this.name = collateral.getName();
        this.description = collateral.getDescription();
        this.type = collateral.getType();
        this.updatedAt = LocalDateTime.now();
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
