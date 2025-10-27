package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.RecordatorioRequestDTO;
import com.proyectoClinica.dto.response.RecordatorioResponseDTO;
import java.util.List;

public interface RecordatorioService {

    RecordatorioResponseDTO crear (RecordatorioRequestDTO requestDTO);
    RecordatorioResponseDTO obtenerPorId (Integer id);
    List<RecordatorioResponseDTO> listar();
    void eliminar (Integer id);
    // Enviar recordatorios pendientes cuya fecha_envio <= now
    void enviarPendientes();
}
