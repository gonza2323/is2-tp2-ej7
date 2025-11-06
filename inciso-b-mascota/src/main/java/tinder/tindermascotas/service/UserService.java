package tinder.tindermascotas.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinder.tindermascotas.entities.Photo;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.entities.Zone;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.repositories.UserRepository;
import tinder.tindermascotas.repositories.ZoneRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private ZoneRepository zoneRepository;

    @Transactional
    public void register(MultipartFile file, String nombre, String apellido, String mail, String clave1, String clave2, String idZona) throws ErrorService {
        if (userRepository.existsByMail(mail))
            throw new ErrorService("Ya existe una cuenta con esa dirección de correo");

        Zone zone = zoneRepository.findById(idZona)
                        .orElseThrow(() -> new ErrorService("La zona no es válida"));

        validate(nombre, apellido, mail, clave1, clave2, zone);

        User user = new User();
        user.setZone(zone);
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setMail(mail);
        user.setClave(clave1);
        user.setAlta(new Date());

        Photo photo = photoService.save(file);
        user.setPhoto(photo);

        userRepository.save(user);

        ///notificationService.send("Bienvenido al Tinder de Mascotas!", "Tinder de Mascotas", user.getMail());
    }

    @Transactional
    public void modify(MultipartFile file, String id, String nombre, String apellido, String mail, String clave1, String clave2, String idZona) throws ErrorService {
        if (userRepository.existsByMailAndIdNot(mail, id))
            throw new ErrorService("Ya existe una cuenta con esa dirección de correo");

        Zone zone = zoneRepository.findById(idZona)
                .orElseThrow(() -> new ErrorService("La zona no es válida"));

        validate(nombre, apellido, mail, clave1, clave2, zone);
        Optional<User> response = userRepository.findById(id);
        if (response.isPresent()) {
            User user = response.get();
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setMail(mail);
            user.setClave(clave1);
            user.setZone(zone);

            String idPhoto = null;
            if (user.getPhoto() != null) {
                idPhoto = user.getPhoto().getId();
            }
            Photo photo = photoService.update(idPhoto, file);
            user.setPhoto(photo);

            userRepository.save(user);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }

    }

    @Transactional
    public void enable(String id) throws ErrorService {
        Optional<User> response = userRepository.findById(id);
        if (response.isPresent()) {
            User user = response.get();
            user.setBaja(null);
            userRepository.save(user);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void disable(String id) throws ErrorService {
        Optional<User> response = userRepository.findById(id);
        if (response.isPresent()) {
            User user = response.get();
            user.setBaja(new Date());
            userRepository.save(user);
        } else {
            throw new ErrorService("No se encontro el usuario solicitado");
        }
    }

    public User searchById(String id) throws ErrorService{
        Optional<User> response = userRepository.findById(id);
        if (response.isPresent()) {
            return response.get();
        } else{
            throw new ErrorService("No existe la mascota solicitada");
        }
    }

    private void validate(String nombre, String apellido, String mail, String clave1, String clave2, Zone zone) throws ErrorService {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorService("El nombre del usuario no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorService("El apellido del usuario no puede ser nulo");
        }
        if (mail == null || mail.isEmpty()) {
            throw new ErrorService("El mail del usuario no puede ser nulo");
        }
        if (clave1 == null || clave1.length() <= 6) {
            throw new ErrorService("La clave del usuario no puede ser nula y tiene que ser de mas de 6 digitos");
        }
        if (!clave1.equals(clave2)) {
            throw new ErrorService("Las claves del usuario no coinciden");
        }
        if (zone == null) {
            throw new ErrorService("Zona no encontrada");
        }
    }
}

