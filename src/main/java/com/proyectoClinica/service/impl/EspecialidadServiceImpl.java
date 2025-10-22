package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.EspecialidadRequestDTO;
import com.proyectoClinica.dto.response.EspecialidadResponseDTO;
import com.proyectoClinica.mapper.EspecialidadMapper;
import com.proyectoClinica.model.Especialidad;
import com.proyectoClinica.repository.EspecialidadRepository;
import com.proyectoClinica.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    @Override
    public EspecialidadResponseDTO crear(EspecialidadRequestDTO requestDTO) {
        Especialidad especialidad = especialidadMapper.toEntity(requestDTO);
        Especialidad guardado = especialidadRepository.save(especialidad);
        return especialidadMapper.toDTO(guardado);
    }

    @Override
    public EspecialidadResponseDTO obtenerPorId(Integer id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrado"));
        return especialidadMapper.toDTO(especialidad);
    }

    @Override
    public List<EspecialidadResponseDTO> listar() {
        return especialidadMapper.toDTOList(especialidadRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        especialidadRepository.deleteById(id);
    }
}
