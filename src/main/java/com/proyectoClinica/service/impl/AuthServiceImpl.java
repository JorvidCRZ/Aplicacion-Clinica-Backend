package com.proyectoClinica.service.impl;


import com.proyectoClinica.dto.request.LoginRequestDTO;
import com.proyectoClinica.dto.response.LoginResponseDTO;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getContrasena().equals(request.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return LoginResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().getNombre())
                .nombre1(usuario.getPersona().getNombre1())
                .nombre2(usuario.getPersona().getNombre2())
                .apellidoPaterno(usuario.getPersona().getApellidoPaterno())
                .apellidoMaterno(usuario.getPersona().getApellidoMaterno())
                .build();
    }
}