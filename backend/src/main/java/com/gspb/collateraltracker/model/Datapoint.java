package com.gspb.collateraltracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "collateralInfo")
public class Datapoint {
    
    @Id
    private String id;
    private String collateralId;
    private String type;
    private String valueType;
    private String displayName;
    private String dependencyExpression;
    private String description;
    private String displayType;
    private List<String> selectableValues;
    private List<String> selectedValues;
    private String status;
    private List<AuditLogEntry> auditLog;
    
    public Datapoint() {
        this.selectableValues = new ArrayList<>();
        this.selectedValues = new ArrayList<>();
        this.auditLog = new ArrayList<>();
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getValueType() {
        return valueType;
    }
    
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDependencyExpression() {
        return dependencyExpression;
    }
    
    public void setDependencyExpression(String dependencyExpression) {
        this.dependencyExpression = dependencyExpression;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDisplayType() {
        return displayType;
    }
    
    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }
    
    public List<String> getSelectableValues() {
        return selectableValues;
    }
    
    public void setSelectableValues(List<String> selectableValues) {
        this.selectableValues = selectableValues;
    }
    
    public List<String> getSelectedValues() {
        return selectedValues;
    }
    
    public void setSelectedValues(List<String> selectedValues) {
        this.selectedValues = selectedValues;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<AuditLogEntry> getAuditLog() {
        return auditLog;
    }
    
    public void setAuditLog(List<AuditLogEntry> auditLog) {
        this.auditLog = auditLog;
    }
}
