package com.proyectoClinica.service.impl;


import com.proyectoClinica.dto.response.MedicoEspecialidadResponseDTO;
import com.proyectoClinica.mapper.MedicoEspecialidadMapper;
import com.proyectoClinica.model.MedicoEspecialidad;
import com.proyectoClinica.repository.MedicoEspecialidadRepository;
import com.proyectoClinica.service.MedicoEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoEspecialidadServiceImpl implements MedicoEspecialidadService {

    private final MedicoEspecialidadRepository medicoEspecialidadRepository;
    private final MedicoEspecialidadMapper medicoEspecialidadMapper;

    @Override
    public MedicoEspecialidadResponseDTO crear(MedicoEspecialidad requestDTO) {
        MedicoEspecialidad medicoEspecialidad = medicoEspecialidadMapper.toEntity(requestDTO);
        MedicoEspecialidad guardado = medicoEspecialidadRepository.save(medicoEspecialidad);
        return medicoEspecialidadMapper.toDTO(guardado);
    }

    @Override
    public MedicoEspecialidadResponseDTO obtenerPorId(Integer id) {
        MedicoEspecialidad medicoEspecialidad = medicoEspecialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico especialidad no encontrado"));
        return medicoEspecialidadMapper.toDTO(medicoEspecialidad);
    }

    @Override
    public List<MedicoEspecialidadResponseDTO> listar() {
        return medicoEspecialidadMapper.toDTOList(medicoEspecialidadRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        medicoEspecialidadRepository.deleteById(id);
    }
}
