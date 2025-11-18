package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;
import com.proyectoClinica.mapper.HistorialMedicoMapper;
import com.proyectoClinica.model.HistorialMedico;
import com.proyectoClinica.repository.HistorialMedicoRepository;
import com.proyectoClinica.service.HistorialMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final HistorialMedicoMapper historialMedicoMapper;

    @Override
    public HistorialMedicoResponseDTO crear(HistorialMedicoRequestDTO requestDTO) {
        HistorialMedico historialMedico = historialMedicoMapper.toEntity(requestDTO);
        HistorialMedico guardado = historialMedicoRepository.save(historialMedico);
        return historialMedicoMapper.toDTO(guardado);
    }

    @Override
    public HistorialMedicoResponseDTO obtenerPorId(Integer id) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial medico no encontrado"));
        return historialMedicoMapper.toDTO(historialMedico);
    }

    @Override
    public List<HistorialMedicoResponseDTO> listar() {
        return historialMedicoMapper.toDTOList(historialMedicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        historialMedicoRepository.deleteById(id);
    }
}
