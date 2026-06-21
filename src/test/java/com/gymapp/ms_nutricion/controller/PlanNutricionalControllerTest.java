package com.gymapp.ms_nutricion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.service.PlanNutricionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlanNutricionalControllerTest {

    private MockMvc mockMvc;

    @Mock private PlanNutricionalService service;
    @InjectMocks private PlanNutricionalController controller;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void crear_PlanValido_Retorna201() throws Exception {

        PlanNutricionalRequestDTO request = new PlanNutricionalRequestDTO(1L, 2L, LocalDate.now(), "Subir masa", 2500, "Huevos y Avena");
        PlanNutricionalResponseDTO response = PlanNutricionalResponseDTO.builder().id(5L).caloriasDiarias(2500).build();

        when(service.crear(any(PlanNutricionalRequestDTO.class))).thenReturn(response);


        mockMvc.perform(post("/api/nutricion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.caloriasDiarias").value(2500));
    }

    @Test
    void crear_CaloriasBajas_Retorna400() throws Exception {

        PlanNutricionalRequestDTO request = new PlanNutricionalRequestDTO(1L, 2L, LocalDate.now(), "Error", 500, "Ayuno");


        mockMvc.perform(post("/api/nutricion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}