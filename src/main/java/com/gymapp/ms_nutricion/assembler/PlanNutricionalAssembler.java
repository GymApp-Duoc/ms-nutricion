package com.gymapp.ms_nutricion.assembler;

import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.model.PlanNutricional;
import org.springframework.stereotype.Component;

@Component
public class PlanNutricionalAssembler {

    public PlanNutricionalResponseDTO toResponseDTO(PlanNutricional plan) {
        if (plan == null) return null;
        return PlanNutricionalResponseDTO.builder()
                .id(plan.getId())
                .miembroId(plan.getMiembroId())
                .nutricionistaId(plan.getNutricionistaId())
                .fechaInicio(plan.getFechaInicio())
                .objetivo(plan.getObjetivo())
                .caloriasDiarias(plan.getCaloriasDiarias())
                .detalleMenu(plan.getDetalleMenu())
                .build();
    }

    public PlanNutricional toEntity(PlanNutricionalRequestDTO dto) {
        if (dto == null) return null;
        PlanNutricional plan = new PlanNutricional();
        plan.setMiembroId(dto.getMiembroId());
        plan.setNutricionistaId(dto.getNutricionistaId());
        plan.setFechaInicio(dto.getFechaInicio());
        plan.setObjetivo(dto.getObjetivo());
        plan.setCaloriasDiarias(dto.getCaloriasDiarias());
        plan.setDetalleMenu(dto.getDetalleMenu());
        plan.setActivo(true);
        return plan;
    }
}
