package com.gspb.collateraltracker.controller;

import com.gspb.collateraltracker.model.Datapoint;
import com.gspb.collateraltracker.service.DatapointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/datapoints")
public class DatapointController {

    @Autowired
    private DatapointService datapointService;

    @PutMapping("/{id}")
    public ResponseEntity<Datapoint> updateDatapoint(@PathVariable String id, @RequestBody Datapoint datapoint) {
        return ResponseEntity.ok(datapointService.updateDatapoint(id, datapoint));
    }
}
