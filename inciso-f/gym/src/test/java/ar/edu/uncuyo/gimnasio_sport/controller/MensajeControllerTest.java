package ar.edu.uncuyo.gimnasio_sport.controller;

import ar.edu.uncuyo.gimnasio_sport.dto.MensajeDTO;
import ar.edu.uncuyo.gimnasio_sport.entity.Mensaje;
import ar.edu.uncuyo.gimnasio_sport.entity.Usuario;
import ar.edu.uncuyo.gimnasio_sport.enums.RolUsuario;
import ar.edu.uncuyo.gimnasio_sport.enums.TipoMensaje;
import ar.edu.uncuyo.gimnasio_sport.repository.UsuarioRepository;
import ar.edu.uncuyo.gimnasio_sport.service.MensajeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MensajeController.class)
class MensajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MensajeService mensajeService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    private Usuario admin;

    @BeforeEach
    void setUp() {
        admin = Usuario.builder()
                .id(1L)
                .nombreUsuario("admin")
                .clave("secret")
                .rol(RolUsuario.ADMINISTRATIVO)
                .eliminado(false)
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATIVO"})
    void mostrarFormularioDeNuevoMensaje() throws Exception {
        when(usuarioRepository.findByNombreUsuarioAndEliminadoFalse("admin")).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/mensajes/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/mensaje/form"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attribute("usuarioActual", samePropertyValuesAs(admin)))
                .andExpect(model().attribute("mensaje", hasProperty("usuarioId", equalTo(admin.getId()))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATIVO"})
    void editarMensajeMuestraDatosDelMensaje() throws Exception {
        when(usuarioRepository.findByNombreUsuarioAndEliminadoFalse("admin")).thenReturn(Optional.of(admin));

        // Autor del mensaje (entidad)
        Usuario autor = Usuario.builder()
                .id(2L)
                .nombreUsuario("autor")
                .clave("pwd")
                .rol(RolUsuario.PROFESOR)
                .eliminado(false)
                .build();

        Long mensajeId = 42L;

        // ENTIDAD Mensaje
        Mensaje mensajeEntity = new Mensaje();
        mensajeEntity.setId(mensajeId);
        mensajeEntity.setAsunto("Titulo");
        mensajeEntity.setCuerpo("Contenido");
        mensajeEntity.setTipo(TipoMensaje.PROMOCION);
        mensajeEntity.setEliminado(false);
        mensajeEntity.setUsuario(autor);

        // DTO
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensajeId);
        dto.setAsunto("Titulo");
        dto.setCuerpo("Contenido");
        dto.setTipo(TipoMensaje.PROMOCION);
        dto.setUsuarioId(autor.getId());
        // nombre / email / fechaProgramada pueden quedar null si tu vista no los usa en edición

        when(mensajeService.obtener(mensajeId)).thenReturn(mensajeEntity);
        when(mensajeService.toDto(mensajeEntity)).thenReturn(dto);

        mockMvc.perform(get("/mensajes/editar/{id}", mensajeId))
                .andExpect(status().isOk())
                .andExpect(view().name("view/mensaje/form"))
                .andExpect(model().attribute("usuarioActual", samePropertyValuesAs(admin)))
                .andExpect(model().attribute("mensaje", samePropertyValuesAs(dto)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATIVO"})
    void crearMensajeRedirigeAlListado() throws Exception {
        when(usuarioRepository.findByNombreUsuarioAndEliminadoFalse("admin")).thenReturn(Optional.of(admin));
        when(mensajeService.crear(any(MensajeDTO.class))).thenReturn(new Mensaje());

        mockMvc.perform(post("/mensajes")
                        .param("asunto", "Promo")                   // <-- nombres alineados al DTO
                        .param("cuerpo", "Descuento especial")
                        .param("tipo", TipoMensaje.PROMOCION.name())
                        .param("usuarioId", "999")                  // será sobreescrito por el controlador si corresponde
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mensajes"));

        verify(mensajeService).crear(argThat(dto -> admin.getId().equals(dto.getUsuarioId())));
    }
}
