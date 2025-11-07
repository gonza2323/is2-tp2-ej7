package com.OPA.demo.service;

import com.OPA.demo.enums.ERole;
import com.OPA.demo.entity.Imagen;
import com.OPA.demo.entity.Usuario;
import com.OPA.demo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(Long dni, String nombre, String telefono, String email, String clave, String clave2, MultipartFile archivo) throws Exception{

        validar(dni, nombre, telefono, email, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
        usuario.setAlta(true);
        usuario.setRol(ERole.USER);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepository.save(usuario);

    }

    public void actualizar(String id, Long dni, String nombre, String telefono,
                           String email, String clave, String clave2, MultipartFile archivo) throws Exception {

        validacionBasica(dni, nombre, telefono, email, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepository.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setDni(dni);
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);

            if (clave != null && !clave.isBlank()) {
                usuario.setClave(new BCryptPasswordEncoder().encode(clave));
            }

            String idImagen = (usuario.getImagen() != null) ? usuario.getImagen().getId() : null;
            Imagen imagen = imagenServicio.actualizar(idImagen, archivo);
            if (imagen != null) {
                usuario.setImagen(imagen);
            }

            usuarioRepository.save(usuario);
        }
    }

    public Usuario getOne(String id){
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario;
    }

    private void validar(Long dni, String nombre, String telefono, String email, String clave, String clave2) throws Exception{


        if(nombre.isEmpty() || email.isEmpty() || clave.isEmpty() || clave2.isEmpty() || dni.toString().isEmpty() || telefono.isEmpty()){
            throw new Exception("Todos los campos son obligatorios");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if(usuario != null){
            throw new Exception("El email ya existe");
        }

        if(clave.length() < 8){
            throw new Exception("La clave debe tener al menos 8 caracteres");
        }

        if(!clave.equals(clave2)){
            throw new Exception("Las claves no coinciden");
        }

    }

    private void validacionBasica(Long dni, String nombre, String telefono, String email, String clave, String clave2) throws Exception {

        if (dni == null || dni <= 0) {
            throw new Exception("El DNI no puede ser nulo ni negativo.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }

        if (telefono == null || telefono.trim().isEmpty()) {
            throw new Exception("El teléfono no puede estar vacío.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email no puede estar vacío.");
        }

        // Si el usuario quiere cambiar la contraseña, debe completar ambas y coincidir
        if ((clave != null && !clave.isEmpty()) || (clave2 != null && !clave2.isEmpty())) {
            if (!clave.equals(clave2)) {
                throw new Exception("Las contraseñas no coinciden.");
            }
            if (clave.length() < 8) {
                throw new Exception("La contraseña debe tener al menos 8 caracteres.");
            }
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuarioSession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);

            return user;
        } else {
            return null;
        }
    }
}
