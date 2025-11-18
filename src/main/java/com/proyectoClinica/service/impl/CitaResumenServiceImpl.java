package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.CitaResumenRequestDTO;
import com.proyectoClinica.dto.response.CitaResumenResponseDTO;
import com.proyectoClinica.model.CitaResumen; // ✅ corregido (antes estaba com.proyectoClinica.entity)
import com.proyectoClinica.mapper.CitaResumenMapper;
import com.proyectoClinica.repository.CitaResumenRepository;
import com.proyectoClinica.service.CitaResumenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaResumenServiceImpl implements CitaResumenService {

    @Autowired
    private CitaResumenRepository citaResumenRepository;

    @Autowired
    private CitaResumenMapper citaResumenMapper;

    @Override
    public List<CitaResumenResponseDTO> listar() {
        return citaResumenRepository.findAll()
                .stream()
                .map(citaResumenMapper::toDTO)
                .toList(); // ✅ puedes usar toList() directamente en Java 17+
    }

    @Override
    public CitaResumenResponseDTO obtenerPorId(Integer id) {
        CitaResumen entity = citaResumenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CitaResumen no encontrada con ID: " + id));
        return citaResumenMapper.toDTO(entity);
    }

    @Override
    public CitaResumenResponseDTO registrar(CitaResumenRequestDTO dto) {
        CitaResumen entity = citaResumenMapper.toEntity(dto);
        CitaResumen guardado = citaResumenRepository.save(entity);
        return citaResumenMapper.toDTO(guardado);
    }

    @Override
    public CitaResumenResponseDTO actualizar(Integer id, CitaResumenRequestDTO dto) {
        CitaResumen existente = citaResumenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CitaResumen no encontrada con ID: " + id));

        // Creamos la entidad actualizada a partir del DTO
        CitaResumen actualizado = citaResumenMapper.toEntity(dto);
        actualizado.setIdCitaResumen(existente.getIdCitaResumen());

        // Guardamos y devolvemos el resultado
        CitaResumen guardado = citaResumenRepository.save(actualizado);
        return citaResumenMapper.toDTO(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        if (!citaResumenRepository.existsById(id)) {
            throw new RuntimeException("No existe una cita resumen con ID: " + id);
        }
        citaResumenRepository.deleteById(id);
    }
}
