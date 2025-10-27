package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.RolRequestDTO;
import com.proyectoClinica.dto.response.RolResponseDTO;
import com.proyectoClinica.mapper.RolMapper;
import com.proyectoClinica.model.Rol;
import com.proyectoClinica.repository.RolRepository;
import com.proyectoClinica.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    public RolResponseDTO crear(RolRequestDTO requestDTO) {
        Rol rol = rolMapper.toEntity(requestDTO);
        Rol guardado = rolRepository.save(rol);
        return rolMapper.toDTO(guardado);
    }

    @Override
    public RolResponseDTO obtenerPorId(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return rolMapper.toDTO(rol);
    }

    @Override
    public List<RolResponseDTO> listar() {
        return rolMapper.toDTOList(rolRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        rolRepository.deleteById(id);
    }
}
