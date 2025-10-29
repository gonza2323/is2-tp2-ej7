package ar.edu.uncuyo.gimnasio_sport.service;

import ar.edu.uncuyo.gimnasio_sport.dto.DepartamentoDto;
import ar.edu.uncuyo.gimnasio_sport.entity.Departamento;
import ar.edu.uncuyo.gimnasio_sport.entity.Provincia;
import ar.edu.uncuyo.gimnasio_sport.error.BusinessException;
import ar.edu.uncuyo.gimnasio_sport.mapper.DepartamentoMapper;
import ar.edu.uncuyo.gimnasio_sport.repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {
    private final DepartamentoRepository departamentoRepository;
    private final ProvinciaService provinciaService;
    private final DepartamentoMapper departamentoMapper;

    @Transactional
    public void crearDepartamento(DepartamentoDto departamentoDto) {
        if (departamentoRepository.existsByNombreAndEliminadoFalse((departamentoDto.getNombre())))
            throw new BusinessException("YaExiste.departamento.nombre");

        Provincia provincia = provinciaService.buscarProvincia(departamentoDto.getProvinciaId());

        Departamento departamento = departamentoMapper.toEntity(departamentoDto);
        departamento.setId(null);
        departamento.setProvincia(provincia);
        departamento.setEliminado(false);
        departamentoRepository.save(departamento);
    }

    public Departamento buscarDepartamento(Long id) {
        return departamentoRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("NoExiste.departamento"));
    }

    public List<DepartamentoDto> buscarDepartamentosPorProvincia(Long provinciaId) {
        List<Departamento> departamentos = departamentoRepository.findAllByProvinciaIdAndEliminadoFalseOrderByNombre(provinciaId);
        return departamentoMapper.toDtos(departamentos);
    }
}
