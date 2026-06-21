package com.gymapp.ms_nutricion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta consolidada del plan nutricional registrado")
public class PlanNutricionalResponseDTO {
    private Long id;
    private Long miembroId;
    private Long nutricionistaId;
    private LocalDate fechaInicio;
    private String objetivo;
    private Integer caloriasDiarias;
    private String detalleMenu;
}