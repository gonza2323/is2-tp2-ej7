package tinder.tindermascotas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private String mime;
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
}
