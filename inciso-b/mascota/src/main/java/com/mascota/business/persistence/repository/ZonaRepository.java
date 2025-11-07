package com.mascota.business.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mascota.business.domain.entity.Zona;



public interface ZonaRepository extends JpaRepository<Zona, String>{
	
	@Query("SELECT c FROM Zona c WHERE c.nombre = :nombre AND c.eliminado = FALSE")
    public Zona buscarZonaPorNombre(@Param("nombre")String id);
	
	@Query("SELECT c FROM Zona c WHERE c.eliminado = FALSE")
    public Collection<Zona> listarZonaActiva();
}

