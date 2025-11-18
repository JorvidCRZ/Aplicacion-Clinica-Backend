package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PagoRequestDTO;
import com.proyectoClinica.dto.response.PagoResponseDTO;
import com.proyectoClinica.mapper.PagoMapper;
import com.proyectoClinica.model.Pago;
import com.proyectoClinica.repository.PagoRepository;
import com.proyectoClinica.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    public PagoResponseDTO crear(PagoRequestDTO requestDTO) {
        Pago pago = pagoMapper.toEntity(requestDTO);
        Pago guardado = pagoRepository.save(pago);
        return pagoMapper.toDTO(guardado);
    }

    @Override
    public PagoResponseDTO obtenerPorId(Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoMapper.toDTO(pago);
    }

    @Override
    public List<PagoResponseDTO> listar() {
        return pagoMapper.toDTOList(pagoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }
}
