package com.OPA.demo.service;

import com.OPA.demo.entity.Imagen;
import com.OPA.demo.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepository imagenRepository;

    public Imagen guardar(MultipartFile file) throws Exception {
        if (file != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(file.getContentType());
                imagen.setNombre(file.getOriginalFilename());
                imagen.setContenido(file.getBytes());
                return imagenRepository.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        } else {
            return null;
        }
    }

    public Imagen actualizar(String id, MultipartFile file) throws Exception{

        Imagen imagen = new Imagen();

        if (file != null) {
            try{

                if (id != null) {
                    Optional<Imagen> imagenOptional = imagenRepository.findById(id);

                    if(imagenOptional.isPresent()){
                        imagen = imagenOptional.get();
                    }
                }

                imagen.setMime(file.getContentType());
                imagen.setNombre(file.getOriginalFilename());
                imagen.setContenido(file.getBytes());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return imagenRepository.save(imagen);
        } else {
            return null;
        }
    }

    public Imagen getOne(String id) {
        Optional<Imagen> respuesta = imagenRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new RuntimeException("No se encontró la imagen con ID: " + id);
        }
    }
}
