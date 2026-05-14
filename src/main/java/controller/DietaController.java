package controller;

import dto.DietaRequestDTO;
import dto.DietaResponseDTO;
import service.DietaServiceInt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutricion")
@RequiredArgsConstructor
public class DietaController {

    private final DietaServiceInt service;

    @GetMapping
    public ResponseEntity<List<DietaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<DietaResponseDTO> obtenerPorMiembroId(@PathVariable Long miembroId) {
        return service.obtenerPorMiembroId(miembroId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DietaResponseDTO> calcularYCrear(@Valid @RequestBody DietaRequestDTO dto) {
        return ResponseEntity.status(201).body(service.crearOCalcular(dto));
    }

    @PutMapping("/miembro/{miembroId}")
    public ResponseEntity<DietaResponseDTO> actualizar(@PathVariable Long miembroId, @Valid @RequestBody DietaRequestDTO dto) {
        return service.actualizar(miembroId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/miembro/{miembroId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long miembroId) {
        service.eliminarPorMiembroId(miembroId);
        return ResponseEntity.noContent().build();
    }
}
