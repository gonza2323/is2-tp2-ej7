package ar.edu.uncuyo.ej2b.dto.usuario;

import ar.edu.uncuyo.ej2b.dto.IdentifiableDto;
import ar.edu.uncuyo.ej2b.enums.UserRole;
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
public class UsuarioDetailDto extends IdentifiableDto<Long> {
    private String email;
    private UserRole rol;
}
