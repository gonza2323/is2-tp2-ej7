package tinder.tindermascotas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tinder.tindermascotas.entities.Vote;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {
    @Query("SELECT v FROM Vote v WHERE v.voterPet.id = :id ORDER BY v.fecha DESC")
    public List<Vote> searchMyVotes(@Param("id") String id);

    @Query("SELECT v FROM Vote v WHERE v.votedPed.id = :id ORDER BY v.fecha DESC")
    public List<Vote> searchRecievedVotes(@Param("id") String id);
}
