package ar.edu.uncuyo.ej2b.dto;

import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDto {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String apellido;

    @NotNull(message = "Debe indicar el DNI")
    private Integer dni;

    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String email;

    @NotBlank(message = "Debe indicar una contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String clave;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String claveConfirmacion;

    @Valid
    private DomicilioDto domicilio = new DomicilioDto();
}
