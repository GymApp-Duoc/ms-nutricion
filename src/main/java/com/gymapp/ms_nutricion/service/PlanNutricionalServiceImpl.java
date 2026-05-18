package com.gymapp.ms_nutricion.service;

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

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarTodos() {
        return repository.findByActivoTrue().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlanNutricionalResponseDTO obtenerPorId(Long id) {
        PlanNutricional plan = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado o inactivo."));
        return mapearADto(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanNutricionalResponseDTO> listarHistorialPorMiembro(Long miembroId) {
        return repository.findByMiembroIdAndActivoTrueOrderByFechaInicioDesc(miembroId).stream()
                .map(this::mapearADto)
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

        PlanNutricional plan = new PlanNutricional(
                null,
                dto.getMiembroId(),
                dto.getNutricionistaId(),
                dto.getFechaInicio(),
                dto.getObjetivo(),
                dto.getCaloriasDiarias(),
                dto.getDetalleMenu(),
                true
        );

        PlanNutricional guardado = repository.save(plan);
        log.info("[NUTRICION] Plan nutricional creado con éxito bajo ID: {}", guardado.getId());

        emitirEventosIntegracion(guardado);
        return mapearADto(guardado);
    }

    @Override
    @Transactional
    public PlanNutricionalResponseDTO actualizar(Long id, PlanNutricionalRequestDTO dto) {
        PlanNutricional existente = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado."));

        if (!existente.getMiembroId().equals(dto.getMiembroId())) {
            throw new BusinessException("Invariabilidad detectada: No se puede cambiar el titular original del plan nutricional.");
        }

        existente.setObjetivo(dto.getObjetivo());
        existente.setCaloriasDiarias(dto.getCaloriasDiarias());
        existente.setDetalleMenu(dto.getDetalleMenu());

        return mapearADto(repository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        PlanNutricional plan = repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plan nutricional no encontrado."));
        plan.setActivo(false); // Borrado lógico robusto
        repository.save(plan);
        log.info("[NUTRICION] Plan nutricional ID {} eliminado lógicamente.", id);
    }

    private void emitirEventosIntegracion(PlanNutricional plan) {
<<<<<<< HEAD

=======
>>>>>>> 02e6e4bee25703bacad01b88f3d59c71fade2b73
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("miembroId", plan.getMiembroId());
            evento.put("accion", "PLAN_NUTRICIONAL_CREADO");
            evento.put("puntosBase", 15);
            gamificacionClient.enviarEvento(evento);
        } catch (Exception e) {
            log.warn("[INTEGRACION] No se pudo conectar con ms-gamificacion: {}", e.getMessage());
        }

<<<<<<< HEAD

=======
>>>>>>> 02e6e4bee25703bacad01b88f3d59c71fade2b73
        try {
            Map<String, Object> noti = new HashMap<>();
            noti.put("miembroId", plan.getMiembroId());
            noti.put("titulo", "Nuevo Plan Nutricional Asignado");
            noti.put("mensaje", "Tu nutricionista ha publicado tu nuevo menú diario de " + plan.getCaloriasDiarias() + " kcal.");
            notificacionClient.enviarNotificacion(noti);
        } catch (Exception e) {
            log.warn("[INTEGRACION] No se pudo despachar la alerta a ms-notificaciones: {}", e.getMessage());
        }
    }

    private PlanNutricionalResponseDTO mapearADto(PlanNutricional entity) {
        return PlanNutricionalResponseDTO.builder()
                .id(entity.getId())
                .miembroId(entity.getMiembroId())
                .nutricionistaId(entity.getNutricionistaId())
                .fechaInicio(entity.getFechaInicio())
                .objetivo(entity.getObjetivo())
                .caloriasDiarias(entity.getCaloriasDiarias())
                .detalleMenu(entity.getDetalleMenu())
                .build();
    }
}
