package com.OPA.demo.repository;

import com.OPA.demo.entity.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, String> {
}
