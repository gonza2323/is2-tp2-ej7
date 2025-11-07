package com.OPA.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "imagenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "mime")
    private String mime;

    @Column(name = "nombre")
    private String nombre;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "contenido")
    private byte[] contenido;

}
