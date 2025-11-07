package com.OPA.demo.controller;

import com.OPA.demo.entity.Editorial;
import com.OPA.demo.service.EditorialServicio;
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
@RequestMapping("/editoriales")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping({"", "/", "/listar"})
    public String listar(Model model) {
        model.addAttribute("editoriales", editorialServicio.listarTodos());
        return "editoriales_listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/crear", "/nuevo"})
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("editorial")) {
            Editorial editorial = new Editorial();
            editorial.setAlta(true);
            model.addAttribute("editorial", editorial);
        }
        return "editorial_form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre,
                          @RequestParam(defaultValue = "false") boolean alta,
                          RedirectAttributes redirectAttributes) {
        try {
            editorialServicio.crear(nombre, alta);
            redirectAttributes.addFlashAttribute("exito", "Editorial creada correctamente.");
            return "redirect:/editoriales/listar";
        } catch (Exception e) {
            Editorial editorial = new Editorial();
            editorial.setNombre(nombre);
            editorial.setAlta(alta);
            redirectAttributes.addFlashAttribute("editorial", editorial);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/editoriales/crear";
        }
    }
}
