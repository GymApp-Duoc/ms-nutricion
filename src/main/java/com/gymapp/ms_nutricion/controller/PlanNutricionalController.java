package com.gymapp.ms_nutricion.controller;

import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.service.PlanNutricionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/nutricion")
@RequiredArgsConstructor
public class PlanNutricionalController {

    private final PlanNutricionalService service;

    @GetMapping
    public ResponseEntity<List<PlanNutricionalResponseDTO>> obtenerTodos() {
        log.info("[CONTROLLER] Solicitando lista de todos los planes nutricionales activos.");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanNutricionalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<PlanNutricionalResponseDTO>> obtenerHistorialPorMiembro(@PathVariable Long miembroId) {
        log.info("[CONTROLLER] Consultando historial nutricional para el miembro ID: {}", miembroId);
        return ResponseEntity.ok(service.listarHistorialPorMiembro(miembroId));
    }

    @PostMapping
    public ResponseEntity<PlanNutricionalResponseDTO> crear(@Valid @RequestBody PlanNutricionalRequestDTO dto) {
        log.info("[CONTROLLER] Solicitud para crear un nuevo plan nutricional recibida. Miembro ID: {}", dto.getMiembroId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanNutricionalResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PlanNutricionalRequestDTO dto) {
        log.info("[CONTROLLER] Actualizando plan nutricional ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("[CONTROLLER] Solicitud de eliminación (baja lógica) para el plan ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

