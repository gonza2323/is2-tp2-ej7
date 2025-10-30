package ar.edu.uncuyo.ej2b.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomicilioDto {
    private Long id;

    @NotBlank(message = "Debe indicar la calle")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String calle;

    @NotNull(message = "Debe indicar la numeración")
    private Integer numeracion;

    @NotNull(message = "Debe seleccionar una localidad")
    private Long localidadId;
}
