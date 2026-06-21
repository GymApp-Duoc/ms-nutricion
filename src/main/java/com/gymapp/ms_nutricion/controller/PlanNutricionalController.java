package com.gymapp.ms_nutricion.controller;

import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.service.PlanNutricionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Nutrición", description = "Endpoints para la gestión y asignación de planes dietéticos")
public class PlanNutricionalController {

    private final PlanNutricionalService service;

    @Operation(summary = "Obtener todos los planes nutricionales activos")
    @GetMapping
    public ResponseEntity<List<PlanNutricionalResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar plan nutricional por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PlanNutricionalResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Historial nutricional de un miembro específico")
    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<PlanNutricionalResponseDTO>> obtenerHistorialPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(service.listarHistorialPorMiembro(miembroId));
    }

    @Operation(summary = "Crear un nuevo plan nutricional")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Miembro no existe o datos inválidos")
    })
    @PostMapping
    public ResponseEntity<PlanNutricionalResponseDTO> crear(@Valid @RequestBody PlanNutricionalRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Actualizar menú u objetivos de un plan")
    @PutMapping("/{id}")
    public ResponseEntity<PlanNutricionalResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PlanNutricionalRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminación lógica de un plan")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Reporte 1: Total de planes nutricionales activos")
    @GetMapping("/reportes/total")
    public ResponseEntity<Long> contarPlanes() {
        return ResponseEntity.ok(service.contarPlanesActivos());
    }

    @Operation(summary = "Reporte 2: Buscar planes por palabra clave en el objetivo (Ej: 'Volumen' o 'Déficit')")
    @GetMapping("/reportes/objetivo/{objetivo}")
    public ResponseEntity<List<PlanNutricionalResponseDTO>> filtrarPorObjetivo(@PathVariable String objetivo) {
        return ResponseEntity.ok(service.listarPlanesPorObjetivo(objetivo));
    }

    @Operation(summary = "Reporte 3: Listar dietas hipercalóricas (mayor o igual a X calorías)")
    @GetMapping("/reportes/calorias")
    public ResponseEntity<List<PlanNutricionalResponseDTO>> filtrarPorCalorias(@RequestParam(defaultValue = "3000") Integer calorias) {
        return ResponseEntity.ok(service.listarPlanesPorCalorias(calorias));
    }

    @Operation(summary = "Reporte 4: Planes asignados en los últimos X días")
    @GetMapping("/reportes/recientes")
    public ResponseEntity<List<PlanNutricionalResponseDTO>> planesRecientes(@RequestParam(defaultValue = "15") int dias) {
        return ResponseEntity.ok(service.listarPlanesRecientes(dias));
    }

    @Operation(summary = "Reporte 5: Conteo de carga de trabajo (planes) por nutricionista")
    @GetMapping("/reportes/nutricionista/{nutricionistaId}/total")
    public ResponseEntity<Long> contarPorNutricionista(@PathVariable Long nutricionistaId) {
        return ResponseEntity.ok(service.contarPlanesPorNutricionista(nutricionistaId));
    }
}
