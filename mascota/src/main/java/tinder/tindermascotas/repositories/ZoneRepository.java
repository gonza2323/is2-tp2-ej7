package tinder.tindermascotas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tinder.tindermascotas.entities.Zone;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends CrudRepository<Zone, String> {

    Zone getZoneById(String id);

    Optional<Zone> findById(String id);

    String id(String id);

    Zone getReferenceById(String id);

    List<Zone> findAll();
}
