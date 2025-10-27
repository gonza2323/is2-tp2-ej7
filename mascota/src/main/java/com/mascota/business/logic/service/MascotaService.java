package com.mascota.business.logic.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mascota.business.domain.entity.Foto;
import com.mascota.business.domain.entity.Mascota;
import com.mascota.business.domain.entity.Usuario;
import com.mascota.business.domain.entity.Zona;
import com.mascota.business.domain.enumeration.Sexo;
import com.mascota.business.domain.enumeration.Tipo;
import com.mascota.business.logic.error.ErrorService;
import com.mascota.business.persistence.repository.MascotaRepository;

import jakarta.persistence.NoResultException;


@Service
public class MascotaService {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private FotoService fotoService;
    
    @Autowired
    private MascotaRepository repository;
    
    public void validar(String nombre, Sexo sexo)throws ErrorService{
      
      try {		
    	  
        if(nombre == null || nombre.trim().isEmpty()){
            throw new ErrorService("Debe indicar el nombre");
        }
        
        if(sexo == null){
            throw new ErrorService("Debe indicar el sexo");
        }
        
      } catch (ErrorService e) {
          throw e;
      } catch (Exception ex){
          ex.printStackTrace();
          throw new ErrorService("Error de Sistemas");
      }   
    }

    @Transactional
    public void crearMascota(MultipartFile archivo,String idUsuario, String nombre, Sexo sexo, Tipo tipo)throws ErrorService{
       
      try {	
    	  
        Usuario usuario = usuarioService.buscarUsuario(idUsuario);
        
        validar(nombre,sexo);
        
        try {
        	Mascota mascotaAux = repository.buscarMascota(idUsuario, nombre);
        	if (mascotaAux != null && !mascotaAux.isEliminado()) {
             throw new ErrorService("Existe una mascota de usted con el nombre indicado");
        	} 
        } catch (NoResultException ex) {}
        
        Mascota mascota = new Mascota();
        mascota.setId(UUID.randomUUID().toString());
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setAlta(new Date());
        mascota.setUsuario(usuario);
        mascota.setTipo(tipo);
        mascota.setEliminado(false);
        
        Foto foto = fotoService.guardar(archivo);
        mascota.setFoto(foto);
        
        
        repository.save(mascota);
        
      }catch(ErrorService e) {  
    	throw e;  
      }catch(Exception e) {
    	e.printStackTrace();
    	throw new ErrorService("Error de Sistemas");
      }
    }
    
    @Transactional
    public void modificarMascota(MultipartFile archivo,String idUsuario,String idMascota, String nombre, Sexo sexo, Tipo tipo)throws ErrorService{
       
       try { 	
    	   
        validar(nombre,sexo);
        
        Mascota mascota = buscarMascota(idMascota);
        
        if(!mascota.getUsuario().getId().equals(idUsuario)){
        	throw new ErrorService("Puede modificar solo mascotas de su pertenencia");	
        }
        
        try {
        	Mascota mascotaAux = repository.buscarMascota(idUsuario, nombre);
        	if (mascotaAux != null && !mascotaAux.isEliminado()) {
             throw new ErrorService("Existe una mascota de usted con el nombre indicado");
        	} 
        } catch (NoResultException ex) {}
        
       
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
                
        String idFoto = null;
        if(mascota.getFoto()!=null){         
            idFoto = mascota.getFoto().getId();
        }
                
        Foto foto = fotoService.actualizar(idFoto, archivo);
        mascota.setFoto(foto);
        mascota.setTipo(tipo);
                
        repository.save(mascota);
                
       }catch(ErrorService e) {  
       		throw e;  
       }catch(Exception e) {
       	  e.printStackTrace();
       	  throw new ErrorService("Error de Sistemas");
       }
    }
    
    @Transactional
    public void eliminar(String idUsuario,String idMascota)throws ErrorService{
       
       try { 
            
            Mascota mascota = buscarMascota(idMascota);
            
            if(!mascota.getUsuario().getId().equals(idUsuario)){
            	throw new ErrorService("Puede modificar solo mascotas de su pertenencia");
            }
            
            mascota.setBaja(new Date());
            
            repository.save(mascota);
          
        }catch(ErrorService e) {  
            throw e;  
        }catch(Exception e) {
        	e.printStackTrace();
        	throw new ErrorService("Error de Sistemas");
        } 
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void crearMascota(String idUsuario, String idMascota) throws ErrorService {
    	
    	 try { 
    		 
    		Usuario usuario = usuarioService.buscarUsuario(idUsuario); 
            Mascota mascota = buscarMascota(idMascota);
            
            if(!mascota.getUsuario().getId().equals(idUsuario)){
              throw new ErrorService("Puede modificar solo mascotas de su pertenencia");
            }
            
            mascota.setUsuario(usuario);
            mascota.setBaja(null);
        	repository.save(mascota);
            
    	}catch(ErrorService e) {  
            throw e;  
        }catch(Exception e) {
        	e.printStackTrace();
        	throw new ErrorService("Error de Sistemas");
        }  

    }
    
    @Transactional(readOnly=true)
    public Mascota buscarMascota(String idMascota) throws ErrorService {

      try { 
    	  
    	  if (idMascota == null || idMascota.trim().isEmpty()) {
              throw new ErrorService("Debe indicar una mascota");
          }

          Optional<Mascota> optional = repository.findById(idMascota);
          Mascota mascota = null;
          if (optional.isPresent()) {
        	  mascota= optional.get();
  			if (mascota.isEliminado()){
                  throw new ErrorService("No se encuentra la mascota indicada");
              }
  		  }
          
          return mascota;
        
      }catch(ErrorService e) {  
  		throw e;  
      }catch(Exception e) {
  	    e.printStackTrace();
  	    throw new ErrorService("Error de Sistemas");
      } 

    }
    
    public Collection<Mascota> listarMascotaPorUsuario(String idUsuario)throws ErrorService {
       
       try {
    	   
    	   if(idUsuario == null || idUsuario.trim().isEmpty()){
               throw new ErrorService("Debe indicar el usuario");
           }
    	   
           return repository.listarMascotasPorUsuario(idUsuario);

       }catch(ErrorService e) {  
     		throw e;    
       }catch(Exception e) {
     	    e.printStackTrace();
     	    throw new ErrorService("Error de Sistemas");
       } 
    }
    
    public Collection<Mascota> listarMascotaDeBaja(String idUsuario)throws ErrorService {

       try { 
    	   
    	   if(idUsuario == null || idUsuario.trim().isEmpty()){
               throw new ErrorService("Debe indicar el usuario");
           }
    	   
    	   return repository.listarMascotasDeBaja(idUsuario);

       }catch(ErrorService e) {  
     		throw e;   
       }catch(Exception e) {
    	    e.printStackTrace();
    	    throw new ErrorService("Error de Sistemas");
       } 
        
    }
    
}

