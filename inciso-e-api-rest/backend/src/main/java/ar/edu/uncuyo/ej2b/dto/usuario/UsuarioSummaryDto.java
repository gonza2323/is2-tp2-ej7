package ar.edu.uncuyo.ej2b.dto.usuario;

import ar.edu.uncuyo.ej2b.dto.IdentifiableDto;
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
public class UsuarioSummaryDto extends IdentifiableDto<Long> {
    private String nombre;
}
