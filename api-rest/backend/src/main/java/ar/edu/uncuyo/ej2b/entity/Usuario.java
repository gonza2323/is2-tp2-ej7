package ar.edu.uncuyo.ej2b.entity;

import ar.edu.uncuyo.ej2b.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends BaseEntity<Long> {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = false)
    private UserRole rol;
}
