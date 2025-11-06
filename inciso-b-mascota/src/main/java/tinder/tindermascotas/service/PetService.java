package tinder.tindermascotas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tinder.tindermascotas.entities.Pet;
import tinder.tindermascotas.entities.Photo;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.enums.Sexo;
import tinder.tindermascotas.enums.Type;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.repositories.PetRepository;
import tinder.tindermascotas.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PhotoService photoService;

    @Transactional
    public void addPet(MultipartFile file, String idUser, String name, Sexo sexo, Type type) throws ErrorService {
        User user = userRepository.findById(idUser).get(); // TODO chequear este opcional, user no utilizado
        validate(name, sexo);
        Pet pet = new Pet();
        pet.setNombre(name);
        pet.setSexo(sexo);
        pet.setUser(user);
        Photo photo = photoService.save(file);
        pet.setPhoto(photo);
        pet.setType(type);
        pet.setAlta(new Date());
        petRepository.save(pet);
    }

    @Transactional
    public void delete(MultipartFile file, String userId, String petId) throws ErrorService {
        Optional<Pet> response = petRepository.findById(petId);
        if (response.isPresent()) {
            Pet pet = response.get();
            if (pet.getUser().getId().equals(userId)) {
                pet.setBaja(new Date());

                String idPhoto = null;
                if (pet.getPhoto() != null) {
                    idPhoto = pet.getPhoto().getId();
                }
                Photo photo = photoService.update(idPhoto, file);
                pet.setPhoto(photo);
                petRepository.save(pet);
            }
        } else
            throw new ErrorService("Mascota no encontrada");
    }

    /// habria que setear el id del usuario en la mascota??
    @Transactional
    public void modify(MultipartFile file,String idUsser, String idPet, String name, Sexo sexo, Type type) throws ErrorService {
        validate(name, sexo);
        Optional<Pet> response = petRepository.findById(idPet);
        if (response.isPresent()) {
            Pet pet = response.get();
            if (pet.getUser().getId().equals(idUsser)) {
                pet.setNombre(name);
                pet.setSexo(sexo);
                Photo photo = photoService.save(file);
                pet.setPhoto(photo);
                pet.setType(type);

                petRepository.save(pet);
            } else {
                throw new ErrorService("No tiene permisos para esta operación");
            }
        } else {
            throw new ErrorService("No existe una mascota con el identificador solicitado");
        }
    }

    public void validate(String name, Sexo sexo) throws ErrorService {
        if (name == null || name.isEmpty()) {
            throw new ErrorService("El nombre de la mascota no puede ser nulo");
        }
        if (sexo == null) {
            throw new ErrorService("El sexo no puede ser nulo");
        }
    }

    @Transactional
    public void darAlta(MultipartFile file, String idUser, String petId, String name, Sexo sexo, Type type) {
        modify(file, idUser, petId, name, sexo, type);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ErrorService("Mascota no encontrada"));

        pet.setBaja(null);
        petRepository.save(pet);
    }

    @Transactional
    public Pet searchById(String idPet) throws ErrorService{
        Optional<Pet> response = petRepository.findById(idPet);
        if (response.isPresent()) {
            return response.get();
        } else{
            throw new ErrorService("No existe la mascota solicitada");
        }
    }

    @Transactional
    public List<Pet> findPetsNotDeleted(String id) {
        return petRepository.findByUserIdAndBajaIsNull(id);
    }

    @Transactional
    public List<Pet> findPetsDeleted(String id) {
        return petRepository.findByUserIdAndBajaIsNotNull(id);
    }
}
