package com.gymapp.ms_nutricion.repository;

import com.gymapp.ms_nutricion.model.PlanNutricional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlanNutricionalRepository extends JpaRepository<PlanNutricional, Long> {


    List<PlanNutricional> findByActivoTrue();


    Optional<PlanNutricional> findByIdAndActivoTrue(Long id);


    List<PlanNutricional> findByMiembroIdAndActivoTrueOrderByFechaInicioDesc(Long miembroId);
}
