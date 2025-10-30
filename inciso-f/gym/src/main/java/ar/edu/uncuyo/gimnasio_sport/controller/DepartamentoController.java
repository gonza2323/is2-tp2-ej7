package ar.edu.uncuyo.gimnasio_sport.controller;

import ar.edu.uncuyo.gimnasio_sport.dto.DepartamentoDto;
import ar.edu.uncuyo.gimnasio_sport.service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    @ResponseBody
    @GetMapping("/departamentos")
    List<DepartamentoDto> buscarProvinciasPorPais(@Param("provinciaId") Long provinciaId) {
        return departamentoService.buscarDepartamentosPorProvincia(provinciaId);
    }
}
