package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;
import com.proyectoClinica.mapper.DetalleCitaMapper;
import com.proyectoClinica.model.DetalleCita;
import com.proyectoClinica.repository.DetalleCitaRepository;
import com.proyectoClinica.service.DetalleCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleCitaServiceImpl implements DetalleCitaService {

    private final DetalleCitaRepository detalleCitaRepository;
    private final DetalleCitaMapper detalleCitaMapper;

    @Override
    public DetalleCitaResponseDTO crear(DetalleCitaRequestDTO requestDTO) {
        DetalleCita detalleCita = detalleCitaMapper.toEntity(requestDTO);
        DetalleCita guardado = detalleCitaRepository.save(detalleCita);
        return detalleCitaMapper.toDTO(guardado);
    }

    @Override
    public DetalleCitaResponseDTO obtenerPorId(Integer id) {
        DetalleCita detalleCita = detalleCitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle cita no encontrado"));
        return detalleCitaMapper.toDTO(detalleCita);
    }

    @Override
    public List<DetalleCitaResponseDTO> listar() {
        return detalleCitaMapper.toDTOList(detalleCitaRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        detalleCitaRepository.deleteById(id);
    }
}
