package com.gspb.collateraltracker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gspb.collateraltracker.model.AuditLogEntry;
import com.gspb.collateraltracker.model.Collateral;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Loads datapoint definitions from datapoints.json file
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadDatapointDefinitions() {
        try {
            InputStream inputStream = new ClassPathResource("datapoints.json").getInputStream();
            return objectMapper.readValue(inputStream, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load datapoints.json", e);
        }
    }
    
    /**
     * Initializes datapoints for a collateral item using definitions from datapoints.json
     */
    public static void initializeDatapoints(Collateral collateral) {
        Map<String, Object> definitions = loadDatapointDefinitions();
        Map<String, Object> datapoints = new HashMap<>();
        
        definitions.forEach((key, value) -> {
            if (!key.startsWith("dp_")) {
                throw new IllegalArgumentException("Datapoint key must start with 'dp_': " + key);
            }
            datapoints.put(key, value);
        });
        
        collateral.setDatapoints(datapoints);
    }
    
    /**
     * Updates a datapoint value and adds an entry to its audit log
     */
    @SuppressWarnings("unchecked")
    public static void updateDatapointValue(Collateral collateral, String datapointKey, String newValue) {
        if (!datapointKey.startsWith("dp_")) {
            throw new IllegalArgumentException("Datapoint key must start with 'dp_'");
        }
        
        Map<String, Object> datapoints = collateral.getDatapoints();
        if (datapoints == null) {
            datapoints = new HashMap<>();
            collateral.setDatapoints(datapoints);
        }
        
        Map<String, Object> datapoint = (Map<String, Object>) datapoints.get(datapointKey);
        if (datapoint == null) {
            throw new IllegalArgumentException("Datapoint not found: " + datapointKey);
        }
        
        List<String> selectedValues = new ArrayList<>();
        selectedValues.add(newValue);
        
        String oldValue = null;
        List<String> currentValues = (List<String>) datapoint.get("selectedValues");
        if (currentValues != null && !currentValues.isEmpty()) {
            oldValue = String.join(",", currentValues);
        }
        
        datapoint.put("selectedValues", selectedValues);
        datapoint.put("status", "completed");
        
        List<Map<String, Object>> auditLog = (List<Map<String, Object>>) datapoint.get("auditLog");
        if (auditLog == null) {
            auditLog = new ArrayList<>();
            datapoint.put("auditLog", auditLog);
        }
        
        Map<String, Object> auditEntry = new HashMap<>();
        auditEntry.put("oldValue", oldValue);
        auditEntry.put("newValue", newValue);
        auditEntry.put("timeChanged", LocalDateTime.now().toString());
        auditLog.add(auditEntry);
    }
}
