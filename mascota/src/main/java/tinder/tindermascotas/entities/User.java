package tinder.tindermascotas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String mail;

    private String clave;

    @ManyToOne
    private Zone zone;

    @OneToOne
    private Photo photo;

    private String provider;

    private String providerUserId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
}
