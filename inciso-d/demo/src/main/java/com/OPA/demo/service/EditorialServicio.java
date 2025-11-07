package com.OPA.demo.service;

import com.OPA.demo.entity.Editorial;
import com.OPA.demo.repository.EditorialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class EditorialServicio {

    @Autowired
    private EditorialRepository editorialRepository;

    // CREATE
    public Editorial crear(String nombre, boolean alta) {
        validarNombre(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre.trim());
        editorial.setAlta(alta);
        return editorialRepository.save(editorial);
    }

    // READ
    @Transactional
    public Editorial obtenerPorId(String id) {
        return editorialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Editorial no encontrada: " + id));
    }

    @Transactional
    public List<Editorial> listarTodos() {
        return editorialRepository.findAll();
    }

    @Transactional
    public List<Editorial> listarActivas() {
        return editorialRepository.findAll().stream().filter(Editorial::isAlta).toList();
        // Si tenés query: return editorialRepository.findByAltaTrue();
    }

    // UPDATE
    public Editorial actualizar(String id, String nombre, Boolean alta) {
        Editorial editorial = obtenerPorId(id);
        if (StringUtils.hasText(nombre)) editorial.setNombre(nombre.trim());
        if (alta != null) editorial.setAlta(alta);
        return editorialRepository.save(editorial);
    }

    // ENABLE / DISABLE
    public void desactivar(String id) {
        Editorial editorial = obtenerPorId(id);
        editorial.setAlta(false);
        editorialRepository.save(editorial);
    }

    public void activar(String id) {
        Editorial editorial = obtenerPorId(id);
        editorial.setAlta(true);
        editorialRepository.save(editorial);
    }

    // DELETE física
    public void eliminar(String id) {
        if (!editorialRepository.existsById(id)) {
            throw new EntityNotFoundException("Editorial no encontrada: " + id);
        }
        editorialRepository.deleteById(id);
    }

    private void validarNombre(String nombre) {
        if (!StringUtils.hasText(nombre)) {
            throw new IllegalArgumentException("El nombre de la editorial es obligatorio");
        }
    }
}
