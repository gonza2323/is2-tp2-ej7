package com.mascota.business.logic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mascota.business.domain.entity.Usuario;
import com.mascota.business.domain.entity.Voto;
import com.mascota.business.domain.entity.Zona;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.persistence.repository.ZonaRepository;

import jakarta.persistence.NoResultException;

@Service
public class ZonaService {

	@Autowired
    private ZonaRepository repository;
	
	public void validar(String nombre, String descripcion) throws ErrorService {
	
		try {
			
	        if (nombre == null || nombre.trim().isEmpty()) {
	            throw new ErrorService("Debe indicar el nombre");
	        }

	        if (descripcion == null || descripcion.trim().isEmpty()) {
	            throw new ErrorService("Debe indicar la descripción");
	        }
	        
		}catch(ErrorService e) {  
		   throw e;  
	    }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorService("Error de Sistemas");
		}   

	}
	
	@Transactional
	public void crearZona(String nombre, String descripcion) throws ErrorService {
		
		try {
	
			validar(nombre, descripcion);
			
			try {
            	Zona zonaAux = repository.buscarZonaPorNombre(nombre);
            	if (zonaAux != null && !zonaAux.isEliminado()) {
                 throw new ErrorService("Existe una zona con el nombre indicado");
            	} 
            } catch (NoResultException ex) {}
			
			Zona zona = new Zona();
			zona.setId(UUID.randomUUID().toString());
			zona.setNombre(nombre);
            zona.setDescripcion(descripcion);
            zona.setEliminado(false);
            
            repository.save(zona);
			
		}catch(ErrorService e) {  
		   throw e;  
		}catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorService("Error de Sistemas");
		}
	}
	
	@Transactional
    public void modificarZona(String idZona, String nombre, String descripcion) throws ErrorService {
		
		try {
	
			validar(nombre, descripcion);
			
			try {
            	Zona zonaAux = repository.buscarZonaPorNombre(nombre);
            	if (zonaAux != null && !zonaAux.isEliminado() && !zonaAux.getId().equals(idZona)) {
                 throw new ErrorService("Existe una zona con el nombre indicado");
            	} 
            } catch (NoResultException ex) {}
			
			Zona zona = buscarZona(idZona);
			zona.setNombre(nombre);
            zona.setDescripcion(descripcion);
            
            repository.save(zona);
			
		}catch(ErrorService e) {  
		   throw e;  
		}catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorService("Error de Sistemas");
		}
	}
    
	@Transactional
    public void eliminarZona(String idZona) throws ErrorService {
		
		try {
	
			
			Zona zona = buscarZona(idZona);
			zona.setEliminado(true);
            
            repository.delete(zona);
			
		}catch(ErrorService e) {  
		   throw e;  
		}catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorService("Error de Sistemas");
		}
	}

    public Zona buscarZona(String idZona) throws ErrorService{

        try {
            
            if (idZona == null || idZona.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la zona");
            }

            Optional<Zona> optional = repository.findById(idZona);
            Zona zona = null;
            if (optional.isPresent()) {
            	zona= optional.get();
    			if (zona == null || zona.isEliminado()){
                    throw new ErrorService("No se encuentra la zona indicada");
                }
    		}
            
            return zona;
            
        } catch (ErrorService ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
    
    @Transactional(readOnly=true)
    public List<Zona> listarZona()throws ErrorService {
 
	  try { 
	 
        return repository.findAll();

      }catch(Exception e) {
   	   e.printStackTrace();
   	   throw new ErrorService("Error de Sistemas");
      }
        
    }
    
    public Collection<Zona> listarZonaActiva()throws ErrorService {
 
	  try { 
		
        return repository.listarZonaActiva();

      }catch(Exception e) {
   	   e.printStackTrace();
   	   throw new ErrorService("Error de Sistemas");
      }
        
    }
}
