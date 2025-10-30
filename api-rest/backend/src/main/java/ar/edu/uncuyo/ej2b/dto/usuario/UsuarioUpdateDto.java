package ar.edu.uncuyo.ej2b.dto.usuario;

import ar.edu.uncuyo.ej2b.dto.IdentifiableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDto extends IdentifiableDto<Long> {
    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String nombre;
}
