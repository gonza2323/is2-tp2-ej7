package ar.edu.uncuyo.ej2b.dto.libro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LibroSummaryDto {
    private Long id;
    private String titulo;
    private Integer fecha;
    private String genero;
    private Integer paginas;
    private Long personaId;
    private String pdfPath;
}
