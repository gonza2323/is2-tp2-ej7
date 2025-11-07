package com.OPA.demo.controller;

import com.OPA.demo.entity.Autor;
import com.OPA.demo.service.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping({"", "/", "/listar"})
    public String listar(Model model) {
        model.addAttribute("autores", autorServicio.listarTodos());
        return "autores_listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/crear", "/nuevo"})
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("autor")) {
            Autor autor = new Autor();
            autor.setAlta(true);
            model.addAttribute("autor", autor);
        }
        return "autor_form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre,
                          @RequestParam(defaultValue = "false") boolean alta,
                          RedirectAttributes redirectAttributes) {
        try {
            autorServicio.crear(nombre, alta);
            redirectAttributes.addFlashAttribute("exito", "Autor creado correctamente.");
            return "redirect:/autores/listar";
        } catch (Exception e) {
            Autor autor = new Autor();
            autor.setNombre(nombre);
            autor.setAlta(alta);
            redirectAttributes.addFlashAttribute("autor", autor);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/autores/crear";
        }
    }
}
