package com.mascota.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mascota.business.domain.entity.Foto;


public interface FotoRepository extends JpaRepository<Foto, String> {}
