package com.OPA.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "libros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "isbn", unique = true)
    private Long isbn;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "anio")
    private int anio;

    @Column(name = "ejemplares")
    private int ejemplares;

    @Column(name = "ejemplaresPrestados")
    private int ejemplaresPrestados;

    @Column(name = "ejemplaresRestantes")
    private int ejemplaresRestantes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id", foreignKey = @ForeignKey(name = "FK_LIBROS_IMAGEN"))
    private Imagen imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editorial_id", nullable = false, foreignKey = @ForeignKey(name = "FK_LIBROS_EDITORIAL"))
    private Editorial editorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false, foreignKey = @ForeignKey(name = "FK_LIBROS_AUTOR"))
    private Autor autor;

    @Column(name = "alta")
    private boolean alta;


}
