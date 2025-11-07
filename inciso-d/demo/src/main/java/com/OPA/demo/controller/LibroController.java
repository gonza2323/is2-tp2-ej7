package com.OPA.demo.controller;

import com.OPA.demo.entity.Libro;
import com.OPA.demo.service.AutorServicio;
import com.OPA.demo.service.EditorialServicio;
import com.OPA.demo.service.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping({"", "/", "/listar"})
    public String listar(Model model) {
        model.addAttribute("libros", libroServicio.listarTodos());
        return "libros_listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/crear", "/nuevo"})
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("libro")) {
            Libro libro = new Libro();
            libro.setAlta(true);
            model.addAttribute("libro", libro);
        }

        model.addAttribute("autores", autorServicio.listarTodos());
        model.addAttribute("editoriales", editorialServicio.listarTodos());
        return "libro_form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/guardar")
    public String guardar(@RequestParam Long isbn,
                          @RequestParam String titulo,
                          @RequestParam Integer anio,
                          @RequestParam Integer ejemplares,
                          @RequestParam(defaultValue = "0") Integer ejemplaresPrestados,
                          @RequestParam(required = false) Integer ejemplaresRestantes,
                          @RequestParam("autorId") String autorId,
                          @RequestParam("editorialId") String editorialId,
                          @RequestParam(defaultValue = "false") boolean alta,
                          @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                          RedirectAttributes redirectAttributes) {

        try {
            libroServicio.crear(
                    isbn,
                    titulo,
                    anio,
                    ejemplares,
                    ejemplaresPrestados,
                    ejemplaresRestantes,
                    alta,
                    autorId,
                    editorialId,
                    archivo
            );
            redirectAttributes.addFlashAttribute("exito", "Libro creado correctamente.");
            return "redirect:/libros/listar";
        } catch (Exception e) {
            Libro libro = new Libro();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio != null ? anio : 0);
            libro.setEjemplares(ejemplares != null ? ejemplares : 0);
            libro.setEjemplaresPrestados(ejemplaresPrestados != null ? ejemplaresPrestados : 0);
            libro.setEjemplaresRestantes(ejemplaresRestantes != null ? ejemplaresRestantes : 0);
            libro.setAlta(alta);

            redirectAttributes.addFlashAttribute("libro", libro);
            redirectAttributes.addFlashAttribute("autorSeleccionado", autorId);
            redirectAttributes.addFlashAttribute("editorialSeleccionada", editorialId);
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/libros/crear";
        }
    }
}
