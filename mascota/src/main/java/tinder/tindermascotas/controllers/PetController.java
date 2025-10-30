package tinder.tindermascotas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tinder.tindermascotas.config.CustomUserDetails;
import tinder.tindermascotas.entities.Pet;
import tinder.tindermascotas.enums.Sexo;
import tinder.tindermascotas.enums.Type;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.service.PetService;
import tinder.tindermascotas.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/mascota")
public class PetController {

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    @GetMapping("/mis-mascotas")
    public String misMascotas(ModelMap model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Pet> mascotas = petService.findPetsNotDeleted(customUserDetails.getId());
        model.put("mascotas", mascotas);
        return "mascotas";
    }

    @GetMapping("/debaja-mascotas")
    public String listarMascotasDeBaja(ModelMap model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Pet> mascotas = petService.findPetsDeleted(customUserDetails.getId());
        model.put("mascotas", mascotas);
        return "mascotasdebaja";
    }

/// ver que editar parece que crea uno nuevo
    @GetMapping("/agregarMascota")
    public String agregar(ModelMap model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.put("sexos", Sexo.values());
        model.put("types", Type.values());
        Pet pet = new Pet();
        model.put("pet", pet);
        model.put("accion", "Crear");
        return "mascota";
    }

    @PostMapping("/actualizar")
    public String actualizar(ModelMap model, MultipartFile file, @RequestParam(required = false) String id,
                             @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String name,
                             @RequestParam Sexo sexo, @RequestParam Type type ){
        try {
            if (id == null || id.isEmpty()) {
                petService.addPet(file, userDetails.getId(), name, sexo, type);
            } else{
                petService.modify(file, userDetails.getId(), id, name, sexo, type);
            }
            return "redirect:/mascota/mis-mascotas";
        } catch (ErrorService e) {
            model.put("sexos", Sexo.values());
            model.put("types", Type.values());
            model.put("error", e.getMessage());
            return "mascota";
        }
    }
    @GetMapping("/{petId}/editar")
    public String editar(ModelMap model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String petId) {
        model.put("sexos", Sexo.values());
        model.put("types", Type.values());
        Pet pet = petService.searchById(petId);
        model.put("pet", pet);
        model.put("accion", "Actualizar");
        return "mascota";
    }

    @PostMapping("/editar")
    public String editar(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam String id,
                         MultipartFile file, @RequestParam String name, @RequestParam Sexo sexo, @RequestParam Type type) {
        petService.modify(file, customUserDetails.getId(), id, name, sexo, type);
        return "redirect:/mascota/mis-mascotas";
    }

    @GetMapping("/{petId}/borrar")
    public String borrar(ModelMap model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String petId) {
        model.put("sexos", Sexo.values());
        model.put("types", Type.values());
        Pet pet = petService.searchById(petId);
        model.put("pet", pet);
        model.put("accion", "Eliminar");
        return "mascota";
    }

    @PostMapping("/borrar")
    public String borrar(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam String id, MultipartFile file) {
        petService.delete(file, customUserDetails.getId(), id);
        return "redirect:/mascota/mis-mascotas";
    }

    @GetMapping("/{petId}/alta")
    public String darAlta(ModelMap model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String petId) {
        model.put("sexos", Sexo.values());
        model.put("types", Type.values());
        Pet pet = petService.searchById(petId);
        model.put("pet", pet);
        model.put("accion", "Alta");
        return "mascota";
    }

    @PostMapping("/alta")
    public String darAlta(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam String id,
                          MultipartFile file, @RequestParam String name, @RequestParam Sexo sexo, @RequestParam Type type) {
        petService.darAlta(file, customUserDetails.getId(), id, name, sexo, type);
        return "redirect:/mascota/mis-mascotas";
    }
}
