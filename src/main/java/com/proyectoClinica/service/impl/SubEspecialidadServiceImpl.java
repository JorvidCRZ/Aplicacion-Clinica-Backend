package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.SubEspecialidadRequestDTO;
import com.proyectoClinica.dto.response.SubEspecialidadResponseDTO;
import com.proyectoClinica.mapper.SubEspecialidadMapper;
import com.proyectoClinica.model.SubEspecialidad;
import com.proyectoClinica.repository.SubEspecialidadRepository;
import com.proyectoClinica.service.SubEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubEspecialidadServiceImpl implements SubEspecialidadService {

    private final SubEspecialidadRepository subEspecialidadRepository;
    private final SubEspecialidadMapper subEspecialidadMapper;

    @Override
    public SubEspecialidadResponseDTO crear(SubEspecialidadRequestDTO requestDTO) {
        SubEspecialidad subEspecialidad = subEspecialidadMapper.toEntity(requestDTO);
        SubEspecialidad guardado = subEspecialidadRepository.save(subEspecialidad);
        return subEspecialidadMapper.toDTO(guardado);
    }

    @Override
    public SubEspecialidadResponseDTO obtenerPorId(Integer id) {
        SubEspecialidad subEspecialidad = subEspecialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subespecialidad no encontrada"));
        return subEspecialidadMapper.toDTO(subEspecialidad);
    }

    @Override
    public List<SubEspecialidadResponseDTO> listar() {
        return subEspecialidadMapper.toDTOList(subEspecialidadRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        subEspecialidadRepository.deleteById(id);
    }

    // 🔹 Nuevo método
    @Override
    public List<SubEspecialidadResponseDTO> listarPorEspecialidad(Integer idEspecialidad) {
        List<SubEspecialidad> lista = subEspecialidadRepository.findByEspecialidad_IdEspecialidad(idEspecialidad);
        return subEspecialidadMapper.toDTOList(lista);
    }
}
