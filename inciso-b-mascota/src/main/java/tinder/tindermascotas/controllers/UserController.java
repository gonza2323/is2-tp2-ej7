package tinder.tindermascotas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tinder.tindermascotas.config.CustomUserDetails;
import tinder.tindermascotas.entities.User;
import tinder.tindermascotas.entities.Zone;
import tinder.tindermascotas.exceptions.ErrorService;
import tinder.tindermascotas.repositories.ZoneRepository;
import tinder.tindermascotas.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ZoneRepository zoneRepository;

    @PostMapping("/registrar")
    public String registrar(ModelMap model, MultipartFile archivo, @RequestParam String nombre,
                            @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1,
                            @RequestParam String clave2, @RequestParam String idZona) {
        try {
            userService.register(archivo, nombre, apellido, mail, clave1, clave2, idZona);
        } catch (ErrorService ex) {
            List<Zone> zonas = (List<Zone>) zoneRepository.findAll();
            model.put("zonas", zonas);
            model.put("error", ex.getMessage());
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("mail", mail);
            model.put("clave1", clave1);
            model.put("clave2", clave2);
            return "registro";
        }
        return "exito";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(@AuthenticationPrincipal CustomUserDetails userDetails, ModelMap model) {
        prepareEditView(model, userDetails.getId());
        return "perfil";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(@AuthenticationPrincipal CustomUserDetails userDetails, ModelMap model, MultipartFile archivo,
                                   @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail,
                                   @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {
        try {
            String userId = userDetails.getId();
            userService.modify(archivo, userId, nombre, apellido, mail, clave1, clave2, idZona);
            return "redirect:/usuario/editar-perfil";
        } catch (ErrorService e) {
            prepareEditView(model, userDetails.getId());
            model.put("error", e.getMessage());
            return "perfil";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            model.put("error", "Error de sistema");
            return "perfil";
        }
    }

    private void prepareEditView(ModelMap model, String userId) {
        List<Zone> zonas = zoneRepository.findAll();
        model.put("zones", zonas);

        User user = userService.searchById(userId);
        model.addAttribute("profile", user);
    }
}
