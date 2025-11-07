package ar.edu.uncuyo.ej2b.dto.libro;

import jakarta.validation.constraints.*;
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
public class LibroCreateDto {
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String titulo;

    @NotNull(message = "Debe indicar el año de publicación")
    @Min(value = 1000, message = "Año debe ser mayor a 999")
    @Max(value = 2100, message = "Año debe ser menor a 2100")
    private Integer fecha;

    @NotBlank(message = "El género no puede estar vacío")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String genero;

    @NotNull(message = "Debe indicar la cantidad de páginas")
    @Min(value = 1, message = "La cantidad de páginas debe ser positiva")
    private Integer paginas;

    @NotNull(message = "Debe indicar la persona dueña del libro")
    private Long personaId;

    @NotNull(message = "Debe indicar el/los autor/es")
    private List<Long> autoresIds = new ArrayList<>();

    private String pdfPath;
}
