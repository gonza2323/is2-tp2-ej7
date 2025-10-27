/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mascota.controller.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mascota.business.domain.entity.Mascota;
import com.mascota.business.domain.entity.Usuario;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.logic.service.MascotaService;
import com.mascota.business.logic.service.UsuarioService;

/**
 *
 * @author IS2
 */
@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MascotaService mascotaService;


    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {

        try {
        	
            Usuario usuario = usuarioService.buscarUsuario(id);
            if (usuario.getFoto() == null) {
                throw new ErrorService("El usuario no tiene una foto asignada.");
            }
            
            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
            
        } catch (ErrorService ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) {

        try {
        	
            Mascota mascota = mascotaService.buscarMascota(id);
            
            if (mascota.getFoto() == null) {
                throw new ErrorService("El usuario no tiene una foto asignada.");
            }
            byte[] foto = mascota.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
            
        } catch (ErrorService ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
