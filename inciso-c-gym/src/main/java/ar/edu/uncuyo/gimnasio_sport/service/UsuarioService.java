package ar.edu.uncuyo.gimnasio_sport.service;

import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioCreateFormDTO;
import ar.edu.uncuyo.gimnasio_sport.dto.UsuarioDetailDto;
import ar.edu.uncuyo.gimnasio_sport.entity.Usuario;
import ar.edu.uncuyo.gimnasio_sport.error.BusinessException;
import ar.edu.uncuyo.gimnasio_sport.mapper.UsuarioMapper;
import ar.edu.uncuyo.gimnasio_sport.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioByUsername(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuarioAndEliminadoFalse(nombreUsuario)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public UsuarioDetailDto buscarUsuarioDtoByUsername(String nombreUsuario) {
        Usuario usuario =  usuarioRepository.findByNombreUsuarioAndEliminadoFalse(nombreUsuario)
                .orElseThrow();

        return usuarioMapper.toDto(usuario);
    }

    public Usuario crearUsuario(UsuarioCreateFormDTO formDto) {
        validarDatos(formDto.getClave(), formDto.getConfirmacionClave());

        if (usuarioRepository.existsByNombreUsuarioAndEliminadoFalse(formDto.getNombreUsuario()))
            throw new BusinessException("YaExiste.usuario.nombre");

        Usuario usuario = usuarioMapper.toEntity(formDto);
        String hashClave = passwordEncoder.encode(formDto.getClave());
        usuario.setClave(hashClave);

        return usuarioRepository.save(usuario);
    }

    private void validarDatos(String clave, String confirmacionClave) {
        if (!clave.equals(confirmacionClave))
            throw new BusinessException("NoIguales.usuario.confirmacionClave");
    }

    public void validarCredenciales(String nombreUsuario, String clave) {
        Usuario usuario = usuarioRepository.findByNombreUsuarioAndEliminadoFalse(nombreUsuario)
                .orElseThrow(() -> new BusinessException("usuario.noEncontrado"));

        if (!passwordEncoder.matches(clave, usuario.getClave())) {
            throw new BusinessException("usuario.claveIncorrecta");
        }
    }
}
