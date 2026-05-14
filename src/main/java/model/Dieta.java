package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dietas")
public class Dieta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "miembro_id", nullable = false, unique = true)
    private Long miembroId;

    @Column(name = "tipo_dieta", nullable = false)
    private String tipoDieta;

    @Column(nullable = false)
    private Integer calorias;

    @Column(nullable = false)
    private Integer proteinas;

    @Column(nullable = false)
    private Integer carbohidratos;

    @Column(nullable = false)
    private Integer grasas;
}
