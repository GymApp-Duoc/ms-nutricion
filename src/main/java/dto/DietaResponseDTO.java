package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietaResponseDTO {
    private Long id;
    private Long miembroId;
    private String tipoDieta;
    private Integer calorias;
    private Integer proteinas;
    private Integer carbohidratos;
    private Integer grasas;
}

