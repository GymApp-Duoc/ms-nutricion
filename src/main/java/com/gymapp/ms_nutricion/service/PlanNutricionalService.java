package com.gymapp.ms_nutricion.service;

import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import java.util.List;

public interface PlanNutricionalService {
    List<PlanNutricionalResponseDTO> listarTodos();
    PlanNutricionalResponseDTO obtenerPorId(Long id);
    List<PlanNutricionalResponseDTO> listarHistorialPorMiembro(Long miembroId);
    PlanNutricionalResponseDTO crear(PlanNutricionalRequestDTO dto);
    PlanNutricionalResponseDTO actualizar(Long id, PlanNutricionalRequestDTO dto);
    void eliminar(Long id);
}