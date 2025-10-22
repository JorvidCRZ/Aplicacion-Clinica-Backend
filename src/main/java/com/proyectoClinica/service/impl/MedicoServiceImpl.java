package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
import com.proyectoClinica.mapper.MedicoMapper;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Override
    public MedicoResponseDTO crear(MedicoRequestDTO requestDTO) {
        Medico medico = medicoMapper.toEntity(requestDTO);
        Medico guardado = medicoRepository.save(medico);
        return medicoMapper.toDTO(guardado);
    }

    @Override
    public MedicoResponseDTO obtenerPorId(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        return medicoMapper.toDTO(medico);
    }

    @Override
    public List<MedicoResponseDTO> listar() {
        return medicoMapper.toDTOList(medicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        medicoRepository.deleteById(id);
    }
}
