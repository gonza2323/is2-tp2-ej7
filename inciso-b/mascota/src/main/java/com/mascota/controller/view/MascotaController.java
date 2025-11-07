/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mascota.controller.view;


import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mascota.business.domain.entity.Mascota;
import com.mascota.business.domain.entity.Usuario;
import com.mascota.business.domain.enumeration.Sexo;
import com.mascota.business.domain.enumeration.Tipo;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.logic.service.MascotaService;
import com.mascota.business.logic.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

/**
 *
 * @author IS2
 */
@Controller
@RequestMapping("/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {

        try {
        	
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaService.eliminar(login.getId(), id);

        } catch (ErrorService ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/mascota/mis-mascotas";
    }
    
    @PostMapping("/alta-perfil")
    public String darAlta(HttpSession session, @RequestParam String id) {

        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaService.crearMascota(login.getId(), id);

        } catch (ErrorService ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);

        }
        return "redirect:/mascota/mis-mascotas";
    }
    
    @GetMapping("/debaja-mascotas")
    public String mascotasDeBaja(HttpSession session, ModelMap model) {

      try {	
    	  
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }

        Collection<Mascota> mascotas = mascotaService.listarMascotaDeBaja(login.getId());
        model.put("mascotas", mascotas);

        return "mascotasdebaja";
        
      }catch(Exception e) {
  	    e.printStackTrace();
  	    return "";
      } 
    }

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap model) {

       try {	
    	   
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }

        Collection<Mascota> mascotas = mascotaService.listarMascotaPorUsuario(login.getId());
        model.put("mascotas", mascotas);

        return "mascotas";
        
       }catch(Exception e) {
     	    e.printStackTrace();
     	    return "";
       } 
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {

        if (accion == null) {
            accion = "Crear";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/login";
        }
        Mascota mascota = new Mascota();
        if (id != null && !id.isEmpty()) {
            try {
                mascota = mascotaService.buscarMascota(id);
            } catch (ErrorService ex) {
                Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.put("perfil", mascota);
        model.put("accion", accion);
        model.put("sexos", Sexo.values());
        model.put("tipos", Tipo.values());
        return "mascota.html";
    }

    @PostMapping("/actualizar-perfil")
    public String crarMascota(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        
        try {
        	
            if (id == null || id.isEmpty()) {
                mascotaService.crearMascota(archivo, login.getId(), nombre, sexo, tipo);
            } else {
            	mascotaService.modificarMascota(archivo, login.getId(), id, nombre, sexo, tipo);
            }

            return "redirect:/inicio";
            
        } catch (ErrorService ex) {

            Mascota mascota = new Mascota();
            mascota.setId(id);
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);

            modelo.put("accion", "Actualizar");
            modelo.put("sexos", Sexo.values());
            modelo.put("tipos", Tipo.values());
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", login);
            
            return "mascota.html";
        }
    }
}
