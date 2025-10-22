package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.mapper.PacienteMapper;
import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public PacienteResponseDTO crear(PacienteRequestDTO requestDTO) {
        Paciente paciente = pacienteMapper.toEntity(requestDTO);
        Paciente guardado = pacienteRepository.save(paciente);
        return pacienteMapper.toDTO(guardado);
    }

    @Override
    public PacienteResponseDTO obtenerPorId(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        return pacienteMapper.toDTO(paciente);
    }

    @Override
    public List<PacienteResponseDTO> listar() {
        return pacienteMapper.toDTOList(pacienteRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }
}
