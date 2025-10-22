package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.mapper.CitaMapper;
import com.proyectoClinica.model.Cita;
import com.proyectoClinica.repository.CitaRepository;
import com.proyectoClinica.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    @Override
    public CitaResponseDTO crear(CitaRequestDTO requestDTO) {
        Cita cita = citaMapper.toEntity(requestDTO);
        Cita guardado = citaRepository.save(cita);
        return citaMapper.toDTO(guardado);
    }

    @Override
    public CitaResponseDTO obtenerPorId(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return citaMapper.toDTO(cita);
    }

    @Override
    public List<CitaResponseDTO> listar() {
        return citaMapper.toDTOList(citaRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        citaRepository.deleteById(id);
    }
}
