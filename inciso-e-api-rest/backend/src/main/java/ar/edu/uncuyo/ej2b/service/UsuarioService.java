package ar.edu.uncuyo.ej2b.service;

import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioCreateDto;
import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioDetailDto;
import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioSummaryDto;
import ar.edu.uncuyo.ej2b.dto.usuario.UsuarioUpdateDto;
import ar.edu.uncuyo.ej2b.entity.Usuario;
import ar.edu.uncuyo.ej2b.error.BusinessException;
import ar.edu.uncuyo.ej2b.mapper.UsuarioMapper;
import ar.edu.uncuyo.ej2b.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<
        Usuario,
        Long,
        UsuarioRepository,
        UsuarioDetailDto,
        UsuarioSummaryDto,
        UsuarioCreateDto,
        UsuarioUpdateDto,
        UsuarioMapper> {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, PasswordEncoder passwordEncoder, AuthService authService) {
        super("Usuario", repository, mapper);
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Override
    protected void preCreate(UsuarioCreateDto dto, Usuario usuario) {
        String passwordHash = passwordEncoder.encode(dto.getClave());
        usuario.setClave(passwordHash);
    }

    @Override
    protected void validateCreate(UsuarioCreateDto dto) {
        validarNombreEsUnico(dto.getEmail(), null);

        if (!dto.getClave().equals(dto.getClaveConfirmacion()))
            throw new BusinessException("Las contraseñas no coinciden");
    }

    @Override
    protected void validateUpdate(UsuarioUpdateDto dto) {
        validarNombreEsUnico(dto.getNombre(), dto.getId());
    }

    private void validarNombreEsUnico(String nombre, Long excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByEmailAndEliminadoFalse(nombre)
                : repository.existsByEmailAndIdNotAndEliminadoFalse(nombre, excludeId);

        if (exists)
            throw new BusinessException("El email de usuario ya está en uso");
    }
}
