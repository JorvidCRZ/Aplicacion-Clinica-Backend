package com.proyectoClinica.service.impl;
import com.proyectoClinica.dto.request.AdminRequestDTO;
import com.proyectoClinica.dto.response.AdminResponseDTO;
import com.proyectoClinica.mapper.AdminMapper;
import com.proyectoClinica.model.Admin;
import com.proyectoClinica.repository.AdminRepository;
import com.proyectoClinica.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Override
    public AdminResponseDTO crear(AdminRequestDTO requestDTO) {
        Admin admin = adminMapper.toEntity(requestDTO);
        Admin guardado = adminRepository.save(admin);
        return adminMapper.toDTO(guardado);
    }

    @Override
    public AdminResponseDTO obtenerPorId(Integer id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));
        return adminMapper.toDTO(admin);
    }

    @Override
    public List<AdminResponseDTO> listar() {
        return adminMapper.toDTOList(adminRepository.findAll()); //
    }

    @Override
    public void eliminar(Integer id) {
        adminRepository.deleteById(id);
    }

}
