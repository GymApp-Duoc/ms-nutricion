package service;

import dto.DietaRequestDTO;
import dto.DietaResponseDTO;

import java.util.List;
import java.util.Optional;

public interface DietaServiceInt {
    List<DietaResponseDTO> listarTodas();
    Optional<DietaResponseDTO> obtenerPorMiembroId(Long miembroId);
    DietaResponseDTO crearOCalcular(DietaRequestDTO dto);
    Optional<DietaResponseDTO> actualizar(Long miembroId, DietaRequestDTO dto);
    void eliminarPorMiembroId(Long miembroId);
}
