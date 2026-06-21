package com.gymapp.ms_nutricion.repository;

import com.gymapp.ms_nutricion.model.PlanNutricional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanNutricionalRepository extends JpaRepository<PlanNutricional, Long> {

    List<PlanNutricional> findByActivoTrue();
    Optional<PlanNutricional> findByIdAndActivoTrue(Long id);
    List<PlanNutricional> findByMiembroIdAndActivoTrueOrderByFechaInicioDesc(Long miembroId);



    // REPORTE 1: Total de planes activos en el gimnasio
    long countByActivoTrue();

    // REPORTE 2: Agrupar/Buscar planes por objetivo
    List<PlanNutricional> findByObjetivoContainingIgnoreCaseAndActivoTrue(String objetivo);

    // REPORTE 3: Buscar planes de alto impacto calórico
    List<PlanNutricional> findByCaloriasDiariasGreaterThanEqualAndActivoTrue(Integer calorias);

    // REPORTE 4: Planes iniciados/creados recientemente
    List<PlanNutricional> findByFechaInicioAfterAndActivoTrue(LocalDate fecha);

    // REPORTE 5: Carga de trabajo por Nutricionista
    long countByNutricionistaIdAndActivoTrue(Long nutricionistaId);
}