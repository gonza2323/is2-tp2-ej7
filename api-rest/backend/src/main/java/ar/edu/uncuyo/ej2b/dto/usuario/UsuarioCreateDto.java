package ar.edu.uncuyo.ej2b.dto.usuario;

import ar.edu.uncuyo.ej2b.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDto {
    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String email;

    @NotBlank(message = "Debe indicar una contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String clave;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private String claveConfirmacion;

    @NotBlank(message = "Debe confirmar la contraseña")
    @Size(min = 8, max = 127, message = "Entre 8 y 127 caracteres")
    private UserRole rol;
}
