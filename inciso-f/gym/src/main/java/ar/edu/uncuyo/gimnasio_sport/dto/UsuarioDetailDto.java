package ar.edu.uncuyo.gimnasio_sport.dto;

import ar.edu.uncuyo.gimnasio_sport.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDetailDto {
    private Long id;
    private String nombreUsuario;
    private String clave;
    private RolUsuario rol;
}
