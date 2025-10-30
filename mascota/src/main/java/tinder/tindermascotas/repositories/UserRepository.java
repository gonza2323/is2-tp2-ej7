package tinder.tindermascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tinder.tindermascotas.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.mail = :mail")
    User searchByMail(@Param("mail") String mail);

    Optional<User> findByMail(String mail);

    boolean existsByMail(String mail);

    boolean existsByMailAndIdNot(String mail, String id);

    Optional<User> findByNombre(String nombre);
    Optional<User> findByProviderUserId(String providerUserId);
}
