package com.mascota.business.logic.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mascota.business.domain.entity.Foto;
import com.mascota.business.domain.entity.Zona;
import com.mascota.business.domain.enumeration.Sexo;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.persistence.repository.FotoRepository;


@Service
public class FotoService {
    
    @Autowired
    private FotoRepository repository;
    
    public void validar(MultipartFile archivo)throws ErrorService{
        
        try {		
      	  
          if(archivo == null || archivo.isEmpty()){
              throw new ErrorService("Debe indicar el archivo");
          }
          
        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }   
      }
    
    @Transactional
    public Foto guardar(MultipartFile archivo)throws ErrorService{
    	
        try {	
    	  
    	  validar(archivo);
      
          Foto foto = new Foto();
          foto.setId(UUID.randomUUID().toString());
          foto.setMime(archivo.getContentType());
          foto.setNombre(archivo.getName());
          foto.setContenido(archivo.getBytes());
          foto.setEliminado(false);
            
          return repository.save(foto);
            
            
        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }  
 
    }
    
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo)throws ErrorService{
    	
    	try {
    		
    		validar(archivo);

            Foto foto = buscarFoto(idFoto);
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            
            return repository.save(foto);
            
    	} catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }      

    }
    
    public Foto buscarFoto(String idFoto) throws ErrorService{

        try {
            
            if (idFoto == null || idFoto.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la foto");
            }

            Optional<Foto> optional = repository.findById(idFoto);
            Foto foto = null;
            if (optional.isPresent()) {
            	foto= optional.get();
    			if (foto.isEliminado()){
                    throw new ErrorService("No se encuentra la foto indicada");
                }
    		}
            
            return foto;
            
        } catch (ErrorService ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
}

