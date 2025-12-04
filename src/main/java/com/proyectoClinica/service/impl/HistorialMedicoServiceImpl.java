package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.HistorialMedicoRequestDTO;
import com.proyectoClinica.dto.request.HistorialMedicoUpdateRequestDTO;
import com.proyectoClinica.dto.response.HistorialMedicoResponseDTO;
import com.proyectoClinica.mapper.HistorialMedicoMapper;
import com.proyectoClinica.model.HistorialMedico;
import com.proyectoClinica.repository.CitaRepository;
import com.proyectoClinica.repository.HistorialMedicoRepository;
import com.proyectoClinica.repository.MedicoRepository;
import com.proyectoClinica.repository.PacienteRepository;
import com.proyectoClinica.service.HistorialMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final HistorialMedicoRepository historialMedicoRepository;
    private final HistorialMedicoMapper historialMedicoMapper;

    @Override
    public HistorialMedicoResponseDTO crear(HistorialMedicoRequestDTO requestDTO) {

        HistorialMedico historial = historialMedicoMapper.toEntity(requestDTO);

        // ► Obtener paciente
        var paciente = pacienteRepository.findById(requestDTO.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        historial.setPaciente(paciente);

        // ► Obtener cita
        var cita = citaRepository.findById(requestDTO.getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        historial.setCita(cita);

        // ► Obtener médico
        var medico = medicoRepository.findById(requestDTO.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        historial.setMedico(medico);

        // Guardar
        HistorialMedico guardado = historialMedicoRepository.save(historial);

        return historialMedicoMapper.toDTO(guardado);
    }


    @Override
    public HistorialMedicoResponseDTO obtenerPorId(Integer id) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial medico no encontrado"));
        return historialMedicoMapper.toDTO(historialMedico);
    }

    @Override
    public List<HistorialMedicoResponseDTO> listar() {
        return historialMedicoMapper.toDTOList(historialMedicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        historialMedicoRepository.deleteById(id);
    }

    @Override
    public HistorialMedicoResponseDTO actualizar(Integer id, HistorialMedicoUpdateRequestDTO dto) {

        HistorialMedico historial = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial médico no encontrado"));

        // Actualizamos solo los campos permitidos
        if (dto.getDiagnostico() != null) {
            historial.setDiagnostico(dto.getDiagnostico());
        }

        if (dto.getObservaciones() != null) {
            historial.setObservaciones(dto.getObservaciones());
        }

        if (dto.getReceta() != null) {
            historial.setReceta(dto.getReceta());
        }

        HistorialMedico actualizado = historialMedicoRepository.save(historial);

        return historialMedicoMapper.toDTO(actualizado);
    }

    @Override
    public List<HistorialMedicoResponseDTO> listarPorPaciente(Integer idPaciente) {
        List<HistorialMedico> historiales =
                historialMedicoRepository.findByPacienteIdPaciente(idPaciente);

        return historialMedicoMapper.toDTOList(historiales);
    }

    @Override
    public List<HistorialMedicoResponseDTO> listarPorMedico(Integer idMedico) {
        List<HistorialMedico> historiales =
                historialMedicoRepository.findByMedicoIdMedico(idMedico);

        return historialMedicoMapper.toDTOList(historiales);
    }

    @Override
    public List<HistorialMedicoResponseDTO> listarPorPacienteYMedico(Integer idPaciente, Integer idMedico) {
        List<HistorialMedico> historiales =
                historialMedicoRepository.findByPacienteIdPacienteAndMedicoIdMedico(idPaciente, idMedico);

        return historialMedicoMapper.toDTOList(historiales);
    }

}
