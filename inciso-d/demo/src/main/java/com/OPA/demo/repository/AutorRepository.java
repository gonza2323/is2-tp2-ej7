package com.OPA.demo.repository;

import com.OPA.demo.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, String> {
}
