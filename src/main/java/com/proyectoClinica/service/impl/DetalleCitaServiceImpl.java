package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;
import com.proyectoClinica.mapper.DetalleCitaMapper;
import com.proyectoClinica.model.DetalleCita;
import com.proyectoClinica.model.MedicoEspecialidad;
import com.proyectoClinica.model.SubEspecialidad;
import com.proyectoClinica.repository.DetalleCitaRepository;
import com.proyectoClinica.repository.MedicoEspecialidadRepository;
import com.proyectoClinica.repository.SubEspecialidadRepository;
import com.proyectoClinica.service.DetalleCitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleCitaServiceImpl implements DetalleCitaService {

    private final DetalleCitaRepository detalleCitaRepository;
    private final DetalleCitaMapper detalleCitaMapper;
    private final MedicoEspecialidadRepository medicoEspecialidadRepository;
    private final SubEspecialidadRepository subEspecialidadRepository;

    @Override
    public DetalleCitaResponseDTO crear(DetalleCitaRequestDTO requestDTO) {
        // Cargar entidades referenciadas y asignarlas
        Integer idMedicoEspecialidad = requestDTO.getIdMedicoEspecialidad();
        MedicoEspecialidad me = medicoEspecialidadRepository.findById(idMedicoEspecialidad)
                .orElseThrow(() -> new RuntimeException("MedicoEspecialidad no encontrado con id: " + idMedicoEspecialidad));

        Integer idSub = requestDTO.getIdSubEspecialidad();
        SubEspecialidad se = null;
        if (idSub != null) {
            se = subEspecialidadRepository.findById(idSub)
                    .orElseThrow(() -> new RuntimeException("SubEspecialidad no encontrada con id: " + idSub));
        }

        DetalleCita detalleCita = detalleCitaMapper.toEntity(requestDTO);
        detalleCita.setMedicoEspecialidad(me);
        detalleCita.setSubEspecialidad(se);

        DetalleCita guardado = detalleCitaRepository.save(detalleCita);
        return detalleCitaMapper.toDTO(guardado);
    }

    @Override
    public DetalleCitaResponseDTO obtenerPorId(Integer id) {
        DetalleCita detalleCita = detalleCitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle cita no encontrado"));
        return detalleCitaMapper.toDTO(detalleCita);
    }

    @Override
    public List<DetalleCitaResponseDTO> listar() {
        return detalleCitaMapper.toDTOList(detalleCitaRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        detalleCitaRepository.deleteById(id);
    }
}
