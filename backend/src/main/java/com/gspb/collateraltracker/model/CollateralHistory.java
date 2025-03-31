package com.gspb.collateraltracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "collateralHistory")
public class CollateralHistory {
    
    @Id
    private String id; // This will be the same as the collateralId (UUID)
    
    private List<CollateralVersion> versions;
    
    public CollateralHistory() {
        this.versions = new ArrayList<>();
    }
    
    public CollateralHistory(String collateralId) {
        this.id = collateralId;
        this.versions = new ArrayList<>();
    }
    
    public void addVersion(Collateral collateral) {
        CollateralVersion version = new CollateralVersion(collateral);
        this.versions.add(version);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public List<CollateralVersion> getVersions() {
        return versions;
    }
    
    public void setVersions(List<CollateralVersion> versions) {
        this.versions = versions;
    }
}
