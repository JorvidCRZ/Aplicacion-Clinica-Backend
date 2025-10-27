package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ReporteRequestDTO;
import com.proyectoClinica.dto.response.ReporteResponseDTO;
import com.proyectoClinica.mapper.ReporteMapper;
import com.proyectoClinica.model.Reporte;
import com.proyectoClinica.repository.ReporteRepository;
import com.proyectoClinica.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    @Override
    public ReporteResponseDTO crear(ReporteRequestDTO requestDTO) {
        Reporte reporte = reporteMapper.toEntity(requestDTO);
        Reporte guardado = reporteRepository.save(reporte);
        return reporteMapper.toDTO(guardado);
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return reporteMapper.toDTO(reporte);
    }

    @Override
    public List<ReporteResponseDTO> listar() {
        return reporteMapper.toDTOList(reporteRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        reporteRepository.deleteById(id);
    }
}
