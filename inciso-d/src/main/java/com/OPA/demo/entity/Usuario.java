package com.OPA.demo.entity;

import com.OPA.demo.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "dni", nullable = false)
    private Long dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "clave")
    private String clave;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id", foreignKey = @ForeignKey(name = "FK_USUARIOS_IMAGEN"))
    private Imagen imagen;

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private ERole rol;

    @Column(name = "alta", nullable = false)
    private boolean alta;

}
