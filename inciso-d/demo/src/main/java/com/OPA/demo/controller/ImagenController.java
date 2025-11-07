package com.OPA.demo.controller;


import com.OPA.demo.entity.Imagen;
import com.OPA.demo.service.ImagenServicio;
import com.OPA.demo.service.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenController {

    @Autowired
    ImagenServicio imagenServicio;


    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        Imagen imagen = imagenServicio.getOne(id);  // ← obtener imagen directamente
        byte[] contenido = imagen.getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(contenido, headers, HttpStatus.OK);
    }


}
