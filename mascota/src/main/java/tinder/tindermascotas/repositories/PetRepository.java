package tinder.tindermascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tinder.tindermascotas.entities.Pet;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, String> {

    @Query("SELECT p FROM Pet p WHERE p.user.id = :id")
    public List<Pet> buscarMascotasPorUsuario(@Param("id") String id);

    List<Pet> findByUserIdAndBajaIsNull(String userId);

    List<Pet> findByUserIdAndBajaIsNotNull(String userId);
}
