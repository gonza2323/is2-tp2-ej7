package com.mascota.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mascota.business.domain.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    
    @Query("SELECT c FROM Usuario c WHERE c.mail = :mail")
    public Usuario buscarUsuarioPorMail(@Param("mail")String mail);
 
    @Query("SELECT c FROM Usuario c WHERE c.mail = :mail AND c.clave = :clave")
    public Usuario buscarUsuarioPorEmailYClave(@Param("mail")String mail, @Param("clave")String clave);
}
