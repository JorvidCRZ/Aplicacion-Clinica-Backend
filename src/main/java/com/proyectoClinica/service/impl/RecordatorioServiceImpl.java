package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.RecordatorioRequestDTO;
import com.proyectoClinica.dto.response.RecordatorioResponseDTO;
import com.proyectoClinica.mapper.RecordatorioMapper;
import com.proyectoClinica.model.Recordatorio;
import com.proyectoClinica.repository.RecordatorioRepository;
import com.proyectoClinica.service.RecordatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordatorioServiceImpl implements RecordatorioService {

    private final RecordatorioRepository recordatorioRepository;
    private final RecordatorioMapper recordatorioMapper;

    @Override
    public RecordatorioResponseDTO crear(RecordatorioRequestDTO requestDTO) {
        Recordatorio recordatorio = recordatorioMapper.toEntity(requestDTO);
        Recordatorio guardado = recordatorioRepository.save(recordatorio);
        return recordatorioMapper.toDTO(guardado);
    }

    @Override
    public RecordatorioResponseDTO obtenerPorId(Integer id) {
        Recordatorio recordatorio = recordatorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recordatorio no encontrado"));
        return recordatorioMapper.toDTO(recordatorio);
    }

    @Override
    public List<RecordatorioResponseDTO> listar() {
        return recordatorioMapper.toDTOList(recordatorioRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        recordatorioRepository.deleteById(id);
    }
}
