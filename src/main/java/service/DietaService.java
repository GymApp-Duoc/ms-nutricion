package service;

import dto.DietaRequestDTO;
import dto.DietaResponseDTO;
import exception.BusinessException;
import exception.RecursoNoEncontradoException;
import model.Dieta;
import repository.DietaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DietaService implements DietaServiceInt {

    private final DietaRepository repository;
    private final RestTemplate restTemplate;

    @Value("${ms.evaluacion.url}")
    private String evaluacionUrl;

    @Override
    @Transactional(readOnly = true)
    public List<DietaResponseDTO> listarTodas() {
        return repository.findAll().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DietaResponseDTO> obtenerPorMiembroId(Long miembroId) {
        return repository.findByMiembroId(miembroId).map(this::mapearADto);
    }

    @Override
    @Transactional
    public DietaResponseDTO crearOCalcular(DietaRequestDTO dto) {
        if (repository.existsByMiembroId(dto.getMiembroId())) {
            throw new BusinessException("El miembro ya tiene una dieta asignada. Use el método de actualizar.");
        }

        // Cumpliendo con la regla de negocio arquitectónica
        validarEvaluacionPrevia(dto.getMiembroId());

        Dieta dieta = new Dieta(null, dto.getMiembroId(), dto.getTipoDieta(),
                dto.getCalorias(), dto.getProteinas(), dto.getCarbohidratos(), dto.getGrasas());

        log.info("Calculando y guardando dieta para el miembro: {}", dto.getMiembroId());
        return mapearADto(repository.save(dieta));
    }

    @Override
    @Transactional
    public Optional<DietaResponseDTO> actualizar(Long miembroId, DietaRequestDTO dto) {
        return repository.findByMiembroId(miembroId).map(existente -> {
            validarEvaluacionPrevia(miembroId); // Validar antes de actualizar

            existente.setTipoDieta(dto.getTipoDieta());
            existente.setCalorias(dto.getCalorias());
            existente.setProteinas(dto.getProteinas());
            existente.setCarbohidratos(dto.getCarbohidratos());
            existente.setGrasas(dto.getGrasas());
            return mapearADto(repository.save(existente));
        });
    }

    @Override
    @Transactional
    public void eliminarPorMiembroId(Long miembroId) {
        Dieta dieta = repository.findByMiembroId(miembroId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe dieta para el miembro: " + miembroId));
        repository.delete(dieta);
    }

    // Regla de Negocio: Validar que el miembro tenga una evaluación vigente en ms-evaluacion
    private void validarEvaluacionPrevia(Long miembroId) {
        try {
            String url = evaluacionUrl + "/api/evaluaciones/miembro/" + miembroId + "/vigente";
            restTemplate.getForObject(url, String.class);
            log.info("Evaluación vigente confirmada para el miembro ID: {}", miembroId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException("Regla de negocio: No se puede calcular macronutrientes. El usuario no tiene un registro actualizado en ms-evaluacion.");
        } catch (RestClientException e) {
            log.warn("Servicio ms-evaluacion no disponible temporalmente. Se permite el cálculo bajo contingencia.");
            // NOTA: Para un entorno estricto lanzaríamos excepción aquí,
            // pero para desarrollo evitamos bloquear si el otro ms está apagado.
        }
    }

    private DietaResponseDTO mapearADto(Dieta dieta) {
        return new DietaResponseDTO(dieta.getId(), dieta.getMiembroId(), dieta.getTipoDieta(),
                dieta.getCalorias(), dieta.getProteinas(), dieta.getCarbohidratos(), dieta.getGrasas());
    }
}
