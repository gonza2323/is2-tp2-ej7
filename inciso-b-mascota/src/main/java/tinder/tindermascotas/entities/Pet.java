package tinder.tindermascotas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import tinder.tindermascotas.enums.Sexo;
import tinder.tindermascotas.enums.Type;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Enumerated(EnumType.STRING)
    private Type type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;

    @ManyToOne
    private User user;
    @OneToOne
    private Photo photo;
}
