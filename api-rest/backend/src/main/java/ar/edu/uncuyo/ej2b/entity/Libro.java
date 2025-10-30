package ar.edu.uncuyo.ej2b.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private Integer fecha;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private Integer paginas;

    private boolean eliminado;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Persona persona;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @Column(name = "pdf_path")
    private String pdfPath;
}
