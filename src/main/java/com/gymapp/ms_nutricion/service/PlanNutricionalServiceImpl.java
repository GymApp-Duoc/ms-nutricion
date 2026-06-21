package com.gymapp.ms_nutricion.service;

import com.gymapp.ms_nutricion.assembler.PlanNutricionalAssembler;
import com.gymapp.ms_nutricion.client.GamificacionClient;
import com.gymapp.ms_nutricion.client.MiembroClient;
import com.gymapp.ms_nutricion.client.NotificacionClient;
import com.gymapp.ms_nutricion.dto.PlanNutricionalRequestDTO;
import com.gymapp.ms_nutricion.dto.PlanNutricionalResponseDTO;
import com.gymapp.ms_nutricion.exception.BusinessException;
import com.gymapp.ms_nutricion.exception.RecursoNoEncontradoException;
import com.gymapp.ms_nutricion.model.PlanNutricional;
import com.gymapp.ms_nutricion.repository.PlanNutricionalRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanNutricionalServiceImpl implements PlanNutricionalService {

    private final PlanNutricionalRepository repository;
    private final MiembroClient miembroClient;
    private final GamificacionClient gamificacionClient;
    private final NotificacionClient notificacionClient;
    private final PlanNutricionalAssembler assembler; // Inyectado para limpieza del código

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarTodos() {
        return repository.findByActivoTrue().stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlanNutricionalResponseDTO obtenerPorId(Long id) {
        PlanNutricional plan = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado."));
        return assembler.toResponseDTO(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarHistorialPorMiembro(Long miembroId) {
        return repository.findByMiembroIdAndActivoTrueOrderByFechaInicioDesc(miembroId).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlanNutricionalResponseDTO crear(PlanNutricionalRequestDTO dto) {
        log.info("[NUTRICION] Intentando asignar plan para miembro ID: {}", dto.getMiembroId());

        try {
            miembroClient.obtenerPorId(dto.getMiembroId());
        } catch (FeignException.NotFound e) {
            log.error("[NUTRICION] No se encuentra el miembro con ID {}", dto.getMiembroId());
            throw new BusinessException("Operación rechazada: El miembro asignado no existe.");
        }

        PlanNutricional plan = assembler.toEntity(dto);
        PlanNutricional guardado = repository.save(plan);

        log.info("[NUTRICION] Plan nutricional creado con éxito ID: {}", guardado.getId());
        emitirEventosIntegracion(guardado);

        return assembler.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PlanNutricionalResponseDTO actualizar(Long id, PlanNutricionalRequestDTO dto) {
        PlanNutricional existente = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado."));

        if (!existente.getMiembroId().equals(dto.getMiembroId())) {
            throw new BusinessException("Invariabilidad: No se puede cambiar el titular original del plan.");
        }

        existente.setObjetivo(dto.getObjetivo());
        existente.setCaloriasDiarias(dto.getCaloriasDiarias());
        existente.setDetalleMenu(dto.getDetalleMenu());

        return assembler.toResponseDTO(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        PlanNutricional plan = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado."));
        plan.setActivo(false);
        repository.save(plan);
    }



    @Override
    @Transactional(readOnly = true)
    public long contarPlanesActivos() {
        return repository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarPlanesPorObjetivo(String objetivo) {
        return repository.findByObjetivoContainingIgnoreCaseAndActivoTrue(objetivo).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarPlanesPorCalorias(Integer caloriasMinimas) {
        return repository.findByCaloriasDiariasGreaterThanEqualAndActivoTrue(caloriasMinimas).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarPlanesRecientes(int dias) {
        LocalDate fechaCorte = LocalDate.now().minusDays(dias);
        return repository.findByFechaInicioAfterAndActivoTrue(fechaCorte).stream()
                .map(assembler::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPlanesPorNutricionista(Long nutricionistaId) {
        return repository.countByNutricionistaIdAndActivoTrue(nutricionistaId);
    }

    private void emitirEventosIntegracion(PlanNutricional plan) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("miembroId", plan.getMiembroId());
            evento.put("accion", "PLAN_NUTRICIONAL_CREADO");
            evento.put("puntosBase", 15);
            gamificacionClient.enviarEvento(evento);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Gamificación fallida: {}", e.getMessage());
        }

        try {
            Map<String, Object> noti = new HashMap<>();
            noti.put("miembroId", plan.getMiembroId());
            noti.put("titulo", "Nuevo Plan Nutricional Asignado");
            noti.put("mensaje", "Tu menú diario de " + plan.getCaloriasDiarias() + " kcal está listo.");
            notificacionClient.enviarNotificacion(noti);
        } catch (Exception e) {
            log.warn("[INTEGRACION] Notificaciones fallida: {}", e.getMessage());
        }
    }
}