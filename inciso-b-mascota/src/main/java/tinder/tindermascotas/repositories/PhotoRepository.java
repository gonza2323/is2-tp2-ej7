package tinder.tindermascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tinder.tindermascotas.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo, String> {
}
