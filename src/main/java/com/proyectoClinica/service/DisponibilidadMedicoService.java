package com.proyectoClinica.service;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.dto.response.ResumenDisponibilidadDTO;

import java.util.List;

public interface DisponibilidadMedicoService {

    DisponibilidadMedicoResponseDTO crear(DisponibilidadMedicoRequestDTO requestDTO);

    DisponibilidadMedicoResponseDTO obtenerPorId(Integer id);

    List<DisponibilidadMedicoResponseDTO> listar();

    void eliminar(Integer id);

    // Nuevos
    List<DisponibilidadMedicoResponseDTO> listarPorMedico(Integer idMedico);

    void actualizarVigenciaTurno(Integer idDisponibilidad, Boolean vigencia);

    void actualizarDiaActivo(Integer idMedico, String diaSemana, Boolean activo);

    // Guardar todo (masivo)
    List<DisponibilidadMedicoResponseDTO> crearMasivo(List<DisponibilidadMedicoRequestDTO> listaRequestDTO);

    // Cálculos
    int calcularMinutosPorDia(Integer idMedico, String diaSemana);
    int calcularMinutosSemana(Integer idMedico);
    int calcularMinutosMes(Integer idMedico); // aproximación usando factor semanas/mes

    // Resumen para dashboard (días activos, minutos semana, minutos mes)
    ResumenDisponibilidadDTO obtenerResumenPorMedico(Integer idMedico);

    // Obtener slots disponibles para una disponibilidad y fecha (formato de fecha: YYYY-MM-DD)
    com.proyectoClinica.dto.response.SlotsDisponibilidadResponseDTO obtenerSlotsDisponibles(Integer idDisponibilidad, java.time.LocalDate fecha);

    List<DisponibilidadMedicoResponseDTO> obtenerDisponibilidadPorMedico(Integer idMedico);
}
