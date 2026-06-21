package com.gymapp.ms_nutricion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Objeto con los datos requeridos para diseñar o actualizar un plan nutricional")
public class PlanNutricionalRequestDTO {

    @NotNull(message = "El ID del miembro es mandatorio")
    @Schema(description = "ID del miembro que recibe el plan", example = "1")
    private Long miembroId;

    @NotNull(message = "El ID del nutricionista es mandatorio")
    @Schema(description = "ID del profesional nutricionista asignado", example = "5")
    private Long nutricionistaId;

    @NotNull(message = "La fecha de inicio es requerida")
    @Schema(description = "Fecha de inicio del ciclo nutricional", example = "2026-06-25")
    private LocalDate fechaInicio;

    @NotBlank(message = "El objetivo del plan no puede ir vacío")
    @Schema(description = "Meta principal del plan dietético", example = "Aumento de masa muscular (Superávit calórico)")
    private String objetivo;

    @NotNull(message = "Las calorías diarias son obligatorias")
    @Min(value = 1000, message = "Las calorías diarias no deben ser menores a 1000 kcal")
    @Max(value = 6000, message = "Las calorías diarias no deben superar las 6000 kcal")
    @Schema(description = "Cantidad de calorías recomendadas por día", example = "2800")
    private Integer caloriasDiarias;

    @Schema(description = "Descripción detallada de las comidas y macros", example = "Desayuno: 4 huevos, 100g avena. Almuerzo: 200g pollo, 150g arroz.")
    private String detalleMenu;
}