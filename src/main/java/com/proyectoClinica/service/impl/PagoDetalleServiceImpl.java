package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;
import com.proyectoClinica.mapper.PagoDetalleMapper;
import com.proyectoClinica.model.PagoDetalle;
import com.proyectoClinica.repository.PagoDetalleRepository;
import com.proyectoClinica.service.PagoDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleServiceImpl implements PagoDetalleService {

    private final PagoDetalleRepository pagoDetalleRepository;
    private final PagoDetalleMapper pagoDetalleMapper;

    @Override
    public PagoDetalleResponseDTO crear(PagoDetalleRequestDTO requestDTO) {
        PagoDetalle pagoDetalle = pagoDetalleMapper.toEntity(requestDTO);
        PagoDetalle guardado = pagoDetalleRepository.save(pagoDetalle);
        return pagoDetalleMapper.toDTO(guardado);
    }

    @Override
    public PagoDetalleResponseDTO obtenerPorId(Integer id) {
        PagoDetalle pagoDetalle = pagoDetalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago detalle no encontrado"));
        return pagoDetalleMapper.toDTO(pagoDetalle);
    }

    @Override
    public List<PagoDetalleResponseDTO> listar() {
        return pagoDetalleMapper.toDTOList(pagoDetalleRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        pagoDetalleRepository.deleteById(id);
    }
}
