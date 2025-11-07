package com.mascota.business.logic.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mascota.business.domain.entity.Mascota;
import com.mascota.business.domain.entity.Voto;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.persistence.repository.VotoRepository;


@Service
public class VotoService {
    
    @Autowired
    private MascotaService mascotaService;
    
    @Autowired
    private VotoRepository repository;
    
    public void validar(String idMascota1, String idMascota2) throws ErrorService{
    	
    	try {
    		
    		if (idMascota1 == null || idMascota1.trim().isEmpty()) {
    	       throw new ErrorService("Debe indicar la mascota ");
    	    }

    	    if (idMascota2 == null || idMascota2.trim().isEmpty()) {
    	       throw new ErrorService("Debe indicar la mascota ");
    	    }

    	    if(idMascota1.equals(idMascota2)){
    	       throw new ErrorService("Una mascota no puede votarse ella misma.");
    	    }
    	        
    	} catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        } 
    }
    
    @Transactional
    public void crearVoto(String idUsuario,String idMascota1, String idMascota2)throws ErrorService{
     
      try {	
        
    	validar(idMascota1,idMascota2);
        
    	Mascota mascota1 = mascotaService.buscarMascota(idMascota1);
        if(!mascota1.getUsuario().getId().equals(idUsuario)){
          throw new ErrorService("El usuario no puede votar su propia mascota");
        }
        
        Mascota mascota2 = mascotaService.buscarMascota(idMascota2);
        
        Voto voto = new Voto();
        voto.setId(UUID.randomUUID().toString());
        voto.setFecha(new Date());
        voto.setMascota1(mascota1);
        voto.setMascota2(mascota2);
        voto.setEliminado(false);
            
        repository.save(voto);
        
      } catch (ErrorService e) {
          throw e;
      } catch (Exception ex){
          ex.printStackTrace();
          throw new ErrorService("Error de Sistemas");
      } 
    }
    
    public Voto buscarVoto(String id) throws ErrorService{

        try {
            
            if (id == null || id.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el voto");
            }

            Optional<Voto> optional = repository.findById(id);
            Voto voto = null;
            if (optional.isPresent()) {
            	voto= optional.get();
    			if (voto.isEliminado()){
                    throw new ErrorService("No se encuentra el voto indicado");
                }
    		}
            
            return voto;
            
        } catch (ErrorService ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
    
    @Transactional
    public void responder(String idUsuario, String idVoto)throws ErrorService{
    	
    	try {
            
            Voto voto = buscarVoto(idVoto);

            if(!voto.getMascota2().getUsuario().getId().equals(idUsuario)){
              throw new ErrorService("El usuario no puede responder así mismo");
            }
            
            voto.setRespuesta(new Date());
            repository.save(voto);
        
    	} catch (ErrorService ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }   
        
    } 
    
}
