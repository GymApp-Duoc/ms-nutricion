package com.gymapp.ms_nutricion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "planes_nutricionales")
@NoArgsConstructor
@AllArgsConstructor
public class PlanNutricional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "miembro_id", nullable = false)
    private Long miembroId;

    @Column(name = "nutricionista_id", nullable = false)
    private Long nutricionistaId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false, length = 100)
    private String objetivo;

    @Column(name = "calorias_diarias", nullable = false)
    private Integer caloriasDiarias;

    @Column(length = 1000)
    private String detalleMenu;

    @Column(nullable = false)
    private boolean activo;
}


