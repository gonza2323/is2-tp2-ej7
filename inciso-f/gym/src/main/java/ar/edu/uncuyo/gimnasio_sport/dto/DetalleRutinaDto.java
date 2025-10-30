package ar.edu.uncuyo.gimnasio_sport.dto;

import ar.edu.uncuyo.gimnasio_sport.enums.EstadoDetalleRutina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleRutinaDto {
    private Long id;

    @NotNull(message = "{NotNull.detalleRutina.fecha}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotBlank(message = "{NotBlank.detalleRutina.actividad}")
    @Size(min = 1, max = 100, message = "{Size.detalleRutina.actividad}")
    private String actividad;

    private EstadoDetalleRutina estadoRutina;

    private Long rutinaId;
}
