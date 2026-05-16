package com.gymapp.ms_nutricion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanNutricionalResponseDTO {
    private Long id;
    private Long miembroId;
    private Long nutricionistaId;
    private LocalDate fechaInicio;
    private String objetivo;
    private Integer caloriasDiarias;
    private String detalleMenu;
}

