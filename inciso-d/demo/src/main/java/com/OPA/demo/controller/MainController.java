package com.OPA.demo.controller;

import com.OPA.demo.entity.Imagen;
import com.OPA.demo.entity.Usuario;
import com.OPA.demo.service.ImagenServicio;
import com.OPA.demo.service.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller

public class MainController {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    ImagenServicio imagenServicio;

    @PreAuthorize( "hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize( "hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logeado = (Usuario) session.getAttribute("usuarioSession");

        if (logeado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        } else {

            return "index";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {

        if (error != null) {
            model.put("error", "Credenciales incorrectas");
        }

        return "login";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/perfil")
    public String perfil(ModelMap model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        if (usuario != null) {
            usuario.setClave(null); // evita que el hash llegue a la vista
        }
        model.put("usuario", usuario);
        return "modificar_perfil";
    }

    @PostMapping("/perfil")
    public String perfil(ModelMap model,
                         HttpSession session,
                         @RequestParam Long dni,
                         @RequestParam String nombre,
                         @RequestParam String telefono,
                         @RequestParam String email,
                         @RequestParam String clave,
                         @RequestParam String clave2,
                         @RequestParam(value = "archivo", required = false) MultipartFile archivo) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        if (usuario == null) {
            model.put("error", "No hay usuario en sesión.");
            return "login";
        }

        try {
            // Llamada al método de actualización pedido
            usuarioServicio.actualizar(
                    usuario.getId(),
                    dni,
                    nombre,
                    telefono,
                    email,
                    clave,
                    clave2,
                    archivo
            );

            // Refrescamos datos básicos en sesión
            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);

            session.setAttribute("usuarioSession", usuario);
            model.put("usuario", usuario);
            model.put("exito", "Perfil actualizado correctamente.");
        } catch (Exception e) {
            model.put("error", e.getMessage());
            model.put("usuario", usuario);
        }

        return "modificar_perfil";
    }



    @GetMapping("/registrar")
    public String registrar() {
        return "registro";
    }

    @PostMapping("/registrar")
    public String registrar(
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String clave,
            @RequestParam String clave2,
            @RequestParam MultipartFile archivo,
            Model model) {
        try {
            usuarioServicio.registrar(dni, nombre, telefono, email, clave, clave2, archivo);
            model.addAttribute("exito", "Usuario registrado correctamente. ¡Ya puedes iniciar sesión!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "registro";
    }
}
