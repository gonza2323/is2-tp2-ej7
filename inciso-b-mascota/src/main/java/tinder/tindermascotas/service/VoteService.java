package tinder.tindermascotas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tinder.tindermascotas.entities.Pet;
import tinder.tindermascotas.entities.Vote;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.repositories.PetRepository;
import tinder.tindermascotas.repositories.VoteRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private NotificationService notificationService;

    public void vote(String userId, String idPet1, String idPet2) throws ErrorService {
        Vote vote = new Vote();
        vote.setFecha(new Date());
        if (idPet1.equals(idPet2)) {
            throw new ErrorService("No puede votarse a si mismo");
        }

        Optional<Pet> response = petRepository.findById(idPet1);
        if (response.isPresent()) {
            Pet pet1 = response.get();
            if (pet1.getUser().getId().equals(userId)) {
                vote.setVoterPet(pet1);
            } else {
                throw new ErrorService("No tiene permisos para realizar esta operación");
            }
        } else {
            throw new ErrorService("No existe una mascota con ese identificador");
        }

        Optional<Pet> response2 = petRepository.findById(idPet2);
        if (response2.isPresent()) {
            Pet pet2 = response2.get();
            vote.setVotedPed(pet2);
            notificationService.send("Su Mascota ha sido votada", "Tinder de Mascotas", pet2.getUser().getMail());

        } else {
            throw new ErrorService("No existe una mascota con ese identificador");
        }

        voteRepository.save(vote);
    }

    public void response(String userId, String voteId) throws ErrorService {
        Optional<Vote> response = voteRepository.findById(voteId);
        if (response.isPresent()) {
            Vote vote = response.get();
            vote.setRespuesta(new Date());

            if (vote.getVotedPed().getUser().getId().equals(userId)) {
                notificationService.send("Tu voto fue correspondido", "Tinder de Mascotas", vote.getVoterPet().getUser().getMail());
                voteRepository.save(vote);
            } else {
                throw new ErrorService("No tiene permisos para realizar esta operacion");
            }
        } else {
            throw new ErrorService("No existe el voto solicitado");
        }
    }

}
