package com.gymapp.ms_nutricion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanNutricionalRequestDTO {

    @NotNull(message = "El ID del miembro es mandatorio")
    private Long miembroId;

    @NotNull(message = "El ID del nutricionista es mandatorio")
    private Long nutricionistaId;

    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaInicio;

    @NotBlank(message = "El objetivo del plan no puede ir vacío")
    private String objetivo;

    @NotNull(message = "Las calorías diarias son obligatorias")
    @Min(value = 1000, message = "Las calorías diarias no deben ser menores a 1000 kcal")
    @Max(value = 6000, message = "Las calorías diarias no deben superar las 6000 kcal")
    private Integer caloriasDiarias;

    private String detalleMenu;
}