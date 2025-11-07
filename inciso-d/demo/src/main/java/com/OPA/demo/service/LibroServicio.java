package com.OPA.demo.service;

import com.OPA.demo.entity.Libro;
import com.OPA.demo.repository.LibroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class LibroServicio {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro obtenerPorId(String id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado: " + id));
    }

    public Libro crear(Long isbn,
                       String titulo,
                       Integer anio,
                       Integer ejemplares,
                       Integer ejemplaresPrestados,
                       Integer ejemplaresRestantes,
                       boolean alta,
                       String autorId,
                       String editorialId,
                       MultipartFile archivo) throws Exception {

        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autorId, editorialId);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo.trim());
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);

        int restantes = ejemplaresRestantes != null
                ? ejemplaresRestantes
                : Math.max(ejemplares - ejemplaresPrestados, 0);
        libro.setEjemplaresRestantes(restantes);

        libro.setAlta(alta);
        libro.setAutor(autorServicio.obtenerPorId(autorId));
        libro.setEditorial(editorialServicio.obtenerPorId(editorialId));

        if (archivo != null && !archivo.isEmpty()) {
            libro.setImagen(imagenServicio.guardar(archivo));
        }

        return libroRepository.save(libro);
    }

    private void validarDatos(Long isbn,
                              String titulo,
                              Integer anio,
                              Integer ejemplares,
                              Integer ejemplaresPrestados,
                              Integer ejemplaresRestantes,
                              String autorId,
                              String editorialId) {

        if (isbn == null || isbn <= 0) {
            throw new IllegalArgumentException("El ISBN es obligatorio y debe ser positivo.");
        }
        if (libroRepository.existsByIsbn(isbn)) {
            throw new IllegalArgumentException("Ya existe un libro registrado con el ISBN proporcionado.");
        }
        if (!StringUtils.hasText(titulo)) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (anio == null || anio <= 0) {
            throw new IllegalArgumentException("El año es obligatorio y debe ser positivo.");
        }
        if (ejemplares == null || ejemplares < 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares no puede ser negativa.");
        }
        if (ejemplaresPrestados == null || ejemplaresPrestados < 0) {
            throw new IllegalArgumentException("La cantidad de ejemplares prestados no puede ser negativa.");
        }
        if (ejemplaresPrestados > ejemplares) {
            throw new IllegalArgumentException("Los ejemplares prestados no pueden superar a los ejemplares totales.");
        }
        if (autorId == null) {
            throw new IllegalArgumentException("Debe seleccionar un autor.");
        }
        if (editorialId == null) {
            throw new IllegalArgumentException("Debe seleccionar una editorial.");
        }

        if (ejemplaresRestantes != null && ejemplaresRestantes < 0) {
            throw new IllegalArgumentException("Los ejemplares restantes no pueden ser negativos.");
        }
    }
}
