package tinder.tindermascotas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tinder.tindermascotas.entities.Pet;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.service.PetService;
import tinder.tindermascotas.service.UserService;
import tinder.tindermascotas.entities.User;


@Controller
@RequestMapping("/photo")
public class PhotoController {
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> petPhoto(@PathVariable String id) {
        try {
            Pet pet = petService.searchById(id);
            if (pet.getPhoto() == null){
                throw new ErrorService("El usuario no tiene una foto asignada");
            }
            byte[] photo = pet.getPhoto().getContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);

        } catch (ErrorService e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<byte[]> userPhoto(@PathVariable String id) {
        try {
            User user = userService.searchById(id);
            if (user.getPhoto() == null){
                throw new ErrorService("El usuario no tiene una foto asignada");
            }
            byte[] photo = user.getPhoto().getContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);

        } catch (ErrorService e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
