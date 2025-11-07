package com.OPA.demo.repository;

import com.OPA.demo.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

    boolean existsByIsbn(Long isbn);
}
