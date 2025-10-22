package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.mapper.DisponibilidadMedicoMapper;
import com.proyectoClinica.model.DisponibilidadMedico;
import com.proyectoClinica.repository.DisponibilidadMedicoRepository;
import com.proyectoClinica.service.DisponibilidadMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadMedicoServiceImpl implements DisponibilidadMedicoService {

    private final DisponibilidadMedicoRepository disponibilidadMedicoRepository;
    private final DisponibilidadMedicoMapper disponibilidadMedicoMapper;

    @Override
    public DisponibilidadMedicoResponseDTO crear(DisponibilidadMedicoRequestDTO requestDTO) {
        DisponibilidadMedico disponibilidadMedico = disponibilidadMedicoMapper.toEntity(requestDTO);
        DisponibilidadMedico guardado = disponibilidadMedicoRepository.save(disponibilidadMedico);
        return disponibilidadMedicoMapper.toDTO(guardado);
    }

    @Override
    public DisponibilidadMedicoResponseDTO obtenerPorId(Integer id) {
        DisponibilidadMedico disponibilidadMedico = disponibilidadMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad medíco no encontrado"));
        return disponibilidadMedicoMapper.toDTO(disponibilidadMedico);
    }

    @Override
    public List<DisponibilidadMedicoResponseDTO> listar() {
        return disponibilidadMedicoMapper.toDTOList(disponibilidadMedicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        disponibilidadMedicoRepository.deleteById(id);
    }
}
