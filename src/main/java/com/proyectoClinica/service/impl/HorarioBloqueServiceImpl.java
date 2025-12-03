package com.proyectoClinica.service.impl;
import com.proyectoClinica.dto.request.HorarioBloqueRequestDTO;
import com.proyectoClinica.dto.response.HorarioBloqueResponseDTO;
import com.proyectoClinica.dto.response.HorariosMedicoResponseDTO;
import com.proyectoClinica.mapper.HorarioBloqueMapper;
import com.proyectoClinica.mapper.HorarioDisponibleMapper;
import com.proyectoClinica.model.HorarioBloque;
import com.proyectoClinica.repository.HorarioBloqueRepository;
import com.proyectoClinica.service.HorarioBloqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioBloqueServiceImpl implements HorarioBloqueService{

    private final HorarioBloqueRepository repository;
    private final HorarioBloqueMapper mapper;
    private final HorarioDisponibleMapper horarioDisponibleMapper;

    @Override
    public HorarioBloqueResponseDTO crear(HorarioBloqueRequestDTO request) {
        HorarioBloque entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public List<HorarioBloqueResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HorarioBloqueResponseDTO obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));
    }

    @Override
    public HorarioBloqueResponseDTO actualizar(Integer id, HorarioBloqueRequestDTO request) {
        HorarioBloque entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));
        HorarioBloque actualizado = mapper.toEntity(request);
        actualizado.setIdBloque(entity.getIdBloque());
        return mapper.toResponse(repository.save(actualizado));
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public HorariosMedicoResponseDTO obtenerHorariosPorMedico(Integer idMedico) {
        List<HorarioBloque> bloques = repository.findHorariosDisponiblesPorMedico(idMedico);
        return horarioDisponibleMapper.toHorariosMedicoResponse(idMedico, bloques);
    }


//
//    @Override
//    public List<HorarioBloque> obtenerBloquesDisponibles(Integer idMedicoEspecialidad, LocalDate fecha) {
//        return List.of();
//    }

}
