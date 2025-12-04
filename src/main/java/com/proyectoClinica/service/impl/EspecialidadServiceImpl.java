package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.EspecialidadRequestDTO;
import com.proyectoClinica.dto.response.EspecialidadResponseDTO;
import com.proyectoClinica.mapper.EspecialidadMapper;
import com.proyectoClinica.model.Especialidad;
import com.proyectoClinica.repository.EspecialidadRepository;
import com.proyectoClinica.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public EspecialidadResponseDTO actualizar(Integer id, EspecialidadRequestDTO requestDTO) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        // Actualizamos campos
        especialidad.setNombre(requestDTO.getNombre());
        especialidad.setDescripcion(requestDTO.getDescripcion());
        especialidad.setUrlImgIcono(requestDTO.getUrlImgIcono());
        especialidad.setUrlImgPort(requestDTO.getUrlImgPort());
        especialidad.setDescripcionPortada(requestDTO.getDescripcionPortada());

        Especialidad actualizada = especialidadRepository.save(especialidad);
        return especialidadMapper.toDTO(actualizada);
    }

    @Override
    public EspecialidadResponseDTO obtenerPorId(Integer id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        return especialidadMapper.toDTO(especialidad);
    }

    @Override
    public List<EspecialidadResponseDTO> listar() {
        return especialidadMapper.toDTOList(especialidadRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        if (!especialidadRepository.existsById(id)) {
            throw new RuntimeException("Especialidad no encontrada");
        }
        especialidadRepository.deleteById(id);
    }
}
