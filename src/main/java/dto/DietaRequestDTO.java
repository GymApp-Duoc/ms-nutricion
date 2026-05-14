package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietaRequestDTO {
    @NotNull(message = "El ID del miembro es obligatorio")
    private Long miembroId;

    @NotBlank(message = "El tipo de dieta es obligatorio (ej. Volumen, Definición)")
    private String tipoDieta;

    @NotNull(message = "Las calorías son obligatorias")
    @Min(value = 500, message = "Las calorías deben ser al menos 500")
    private Integer calorias;

    @NotNull(message = "Los gramos de proteína son obligatorios")
    private Integer proteinas;

    @NotNull(message = "Los gramos de carbohidratos son obligatorios")
    private Integer carbohidratos;

    @NotNull(message = "Los gramos de grasas son obligatorios")
    private Integer grasas;
}
