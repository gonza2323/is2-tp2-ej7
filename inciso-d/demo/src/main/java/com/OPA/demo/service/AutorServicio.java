package com.OPA.demo.service;

import com.OPA.demo.entity.Autor;
import com.OPA.demo.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class AutorServicio {

    @Autowired
    private AutorRepository autorRepository;


    // CREATE
    public Autor crear(String nombre, boolean alta) {
        validarNombre(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre.trim());
        autor.setAlta(alta);
        return autorRepository.save(autor);
    }

    // READ
    @Transactional
    public Autor obtenerPorId(String id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado: " + id));
    }

    @Transactional
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    @Transactional
    public List<Autor> listarActivos() {
        return autorRepository.findAll().stream().filter(Autor::isAlta).toList();
        // Si tenés query: return autorRepository.findByAltaTrue();
    }

    // UPDATE
    public Autor actualizar(String id, String nombre, Boolean alta) {
        Autor autor = obtenerPorId(id);
        if (StringUtils.hasText(nombre)) autor.setNombre(nombre.trim());
        if (alta != null) autor.setAlta(alta);
        return autorRepository.save(autor);
    }

    // ENABLE / DISABLE (baja/alta lógica)
    public void desactivar(String id) {
        Autor autor = obtenerPorId(id);
        autor.setAlta(false);
        autorRepository.save(autor);
    }

    public void activar(String id) {
        Autor autor = obtenerPorId(id);
        autor.setAlta(true);
        autorRepository.save(autor);
    }

    // DELETE (física; si preferís solo baja lógica, no uses esto)
    public void eliminar(String id) {
        if (!autorRepository.existsById(id)) {
            throw new EntityNotFoundException("Autor no encontrado: " + id);
        }
        autorRepository.deleteById(id);
    }

    private void validarNombre(String nombre) {
        if (!StringUtils.hasText(nombre)) {
            throw new IllegalArgumentException("El nombre del autor es obligatorio");
        }
    }
}
