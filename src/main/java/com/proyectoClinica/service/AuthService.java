package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.LoginRequestDTO;
import com.proyectoClinica.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}