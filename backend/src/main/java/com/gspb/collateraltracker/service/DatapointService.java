package com.gspb.collateraltracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gspb.collateraltracker.model.AuditLogEntry;
import com.gspb.collateraltracker.model.Datapoint;
import com.gspb.collateraltracker.repository.DatapointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatapointService {

    @Autowired
    private DatapointRepository datapointRepository;

    private Map<String, Object> datapointDefinitions;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("datapoints.json").getInputStream();
            datapointDefinitions = mapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load datapoints.json", e);
        }
    }

    public List<Datapoint> getDatapointsForCollateral(String collateralId) {
        return datapointRepository.findByCollateralId(collateralId);
    }

    public void createDatapointsForCollateral(String collateralId) {
        datapointDefinitions.forEach((key, value) -> {
            Datapoint datapoint = new Datapoint();
            datapoint.setId(key + "_" + collateralId);
            datapoint.setCollateralId(collateralId);
            
            Map<String, Object> definition = (Map<String, Object>) value;
            datapoint.setType((String) definition.get("type"));
            datapoint.setValueType((String) definition.get("valueType"));
            datapoint.setDisplayName((String) definition.get("displayName"));
            datapoint.setDependencyExpression((String) definition.get("dependencyExpression"));
            datapoint.setDescription((String) definition.get("description"));
            datapoint.setDisplayType((String) definition.get("displayType"));
            
            if (definition.get("selectableValues") != null) {
                datapoint.setSelectableValues(new ArrayList<>((List<String>) definition.get("selectableValues")));
            }
            
            datapoint.setSelectedValues(new ArrayList<>((List<String>) definition.get("selectedValues")));
            datapoint.setStatus((String) definition.get("status"));
            datapoint.setAuditLog(new ArrayList<>());
            
            datapointRepository.save(datapoint);
        });
    }

    public Datapoint updateDatapoint(String id, Datapoint datapoint) {
        return datapointRepository.findById(id)
                .map(existingDatapoint -> {
                    if (datapoint.getSelectedValues() != null && 
                        !datapoint.getSelectedValues().equals(existingDatapoint.getSelectedValues())) {
                        AuditLogEntry logEntry = new AuditLogEntry();
                        logEntry.setOldValue(String.join(",", existingDatapoint.getSelectedValues()));
                        logEntry.setNewValue(String.join(",", datapoint.getSelectedValues()));
                        logEntry.setTimeChanged(LocalDateTime.now());
                        existingDatapoint.getAuditLog().add(logEntry);
                    }
                    
                    existingDatapoint.setSelectedValues(datapoint.getSelectedValues());
                    existingDatapoint.setStatus(datapoint.getStatus());
                    
                    return datapointRepository.save(existingDatapoint);
                })
                .orElseThrow(() -> new IllegalArgumentException("Datapoint not found: " + id));
    }
}
