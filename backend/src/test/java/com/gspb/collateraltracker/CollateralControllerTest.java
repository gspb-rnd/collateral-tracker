package com.gspb.collateraltracker;

import com.gspb.collateraltracker.controller.CollateralController;
import com.gspb.collateraltracker.model.Collateral;
import com.gspb.collateraltracker.repository.CollateralRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CollateralController.class)
public class CollateralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollateralRepository collateralRepository;

    @Test
    public void testGetAllCollateral() throws Exception {
        Collateral collateral1 = new Collateral();
        collateral1.setId("1");
        collateral1.setName("Test Collateral 1");
        
        Collateral collateral2 = new Collateral();
        collateral2.setId("2");
        collateral2.setName("Test Collateral 2");
        
        when(collateralRepository.findAll()).thenReturn(Arrays.asList(collateral1, collateral2));
        
        mockMvc.perform(get("/api/collateral"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Collateral 1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Test Collateral 2"));
    }

    @Test
    public void testGetCollateralById() throws Exception {
        Collateral collateral = new Collateral();
        collateral.setId("1");
        collateral.setName("Test Collateral");
        
        when(collateralRepository.findById("1")).thenReturn(Optional.of(collateral));
        
        mockMvc.perform(get("/api/collateral/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Collateral"));
    }

    @Test
    public void testSearchCollateral() throws Exception {
        Collateral collateral = new Collateral();
        collateral.setId("1");
        collateral.setName("Test Collateral");
        
        when(collateralRepository.findByNameContainingIgnoreCase("Test")).thenReturn(Arrays.asList(collateral));
        
        mockMvc.perform(get("/api/collateral/search?query=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Collateral"));
    }
}
