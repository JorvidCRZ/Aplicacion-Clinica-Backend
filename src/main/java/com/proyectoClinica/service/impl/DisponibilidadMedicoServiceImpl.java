package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.DisponibilidadMedicoRequestDTO;
import com.proyectoClinica.dto.response.DisponibilidadMedicoResponseDTO;
import com.proyectoClinica.mapper.DisponibilidadMedicoMapper;
import com.proyectoClinica.model.Medico;
import com.proyectoClinica.repository.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.proyectoClinica.model.DisponibilidadMedico;
import com.proyectoClinica.repository.DisponibilidadMedicoRepository;
import com.proyectoClinica.service.DisponibilidadMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import com.proyectoClinica.model.Cita;
import com.proyectoClinica.dto.response.SlotsDisponibilidadResponseDTO;
import com.proyectoClinica.dto.response.ResumenDisponibilidadDTO;

@Service
@RequiredArgsConstructor
public class DisponibilidadMedicoServiceImpl implements DisponibilidadMedicoService {

    private final DisponibilidadMedicoRepository disponibilidadMedicoRepository;
    private final DisponibilidadMedicoMapper disponibilidadMedicoMapper;
    private final MedicoRepository medicoRepository;
    private final com.proyectoClinica.repository.CitaRepository citaRepository;

    // Factor promedio semanas por mes para aproximación
    private static final double SEMANAS_POR_MES = 4.34524;

    // ------------------ CRUD básico ------------------

    @Override
    @Transactional
    public DisponibilidadMedicoResponseDTO crear(DisponibilidadMedicoRequestDTO requestDTO) {

        // Validar horas
        if(requestDTO.getHoraFin().isBefore(requestDTO.getHoraInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora de fin debe ser posterior a la hora de inicio");
        }

        // Validar solapamiento
        List<DisponibilidadMedico> existentes = disponibilidadMedicoRepository
                .findByMedico_IdMedicoAndDiaSemanaAndVigenciaTrue(requestDTO.getIdMedico(), requestDTO.getDiaSemana());

        boolean haySolapamiento = existentes.stream().anyMatch(d ->
                !requestDTO.getHoraFin().isBefore(d.getHoraInicio()) &&
                        !requestDTO.getHoraInicio().isAfter(d.getHoraFin())
        );

        if (haySolapamiento) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El horario se solapa con otro turno existente para el mismo día.");
        }

        // Convertir DTO a entidad
        DisponibilidadMedico entidad = disponibilidadMedicoMapper.toEntity(requestDTO);

        // Cargar Medico
        Medico medico = medicoRepository.findById(requestDTO.getIdMedico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado"));
        entidad.setMedico(medico);

        // Definir duración de cada slot
        if (requestDTO.getDuracionMinutos() != null && requestDTO.getDuracionMinutos() > 0) {
            entidad.setDuracionMinutos(requestDTO.getDuracionMinutos());
        } else {
            entidad.setDuracionMinutos(30); // default 30 minutos
        }

        // Guardar en DB -> Trigger de PostgreSQL genera automáticamente los slots
        DisponibilidadMedico guardado = disponibilidadMedicoRepository.save(entidad);

        return disponibilidadMedicoMapper.toDTO(guardado);
    }

    @Override
    public ResumenDisponibilidadDTO obtenerResumenPorMedico(Integer idMedico) {
    List<DisponibilidadMedico> lista = disponibilidadMedicoRepository.findByMedico_IdMedicoAndVigenciaTrue(idMedico);

    int diasActivos = (int) lista.stream()
        .filter(d -> Boolean.TRUE.equals(d.getDiaActivo()) && Boolean.TRUE.equals(d.getVigencia()))
        .map(DisponibilidadMedico::getDiaSemana)
        .distinct()
        .count();

    int minutosSemana = calcularMinutosSemana(idMedico);
    int minutosMes = calcularMinutosMes(idMedico);

    return ResumenDisponibilidadDTO.builder()
        .diasActivos(diasActivos)
        .minutosSemana(minutosSemana)
        .minutosMes(minutosMes)
        .build();
    }

    @Override
    public DisponibilidadMedicoResponseDTO obtenerPorId(Integer id) {
    DisponibilidadMedico disponibilidadMedico = disponibilidadMedicoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad del médico no encontrada"));
        return disponibilidadMedicoMapper.toDTO(disponibilidadMedico);
    }

    @Override
    public List<DisponibilidadMedicoResponseDTO> listar() {
        return disponibilidadMedicoMapper.toDTOList(disponibilidadMedicoRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        if (!disponibilidadMedicoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un horario con el ID proporcionado");
        }
        disponibilidadMedicoRepository.deleteById(id);
    }

    // ------------------ Nuevos métodos ------------------

    @Override
    public List<DisponibilidadMedicoResponseDTO> listarPorMedico(Integer idMedico) {
        List<DisponibilidadMedico> lista = disponibilidadMedicoRepository.findByMedico_IdMedico(idMedico);
        return disponibilidadMedicoMapper.toDTOList(lista);
    }

    @Override
    @Transactional
    public void actualizarVigenciaTurno(Integer idDisponibilidad, Boolean vigencia) {
    DisponibilidadMedico disponibilidad = disponibilidadMedicoRepository.findById(idDisponibilidad)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe disponibilidad con ese ID"));

    disponibilidad.setVigencia(vigencia);
    disponibilidadMedicoRepository.save(disponibilidad);
    }

    @Override
    @Transactional
    public void actualizarDiaActivo(Integer idMedico, String diaSemana, Boolean activo) {
    List<DisponibilidadMedico> listaTurnos = disponibilidadMedicoRepository.findByMedico_IdMedicoAndDiaSemana(idMedico, diaSemana);

        if (listaTurnos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El médico no tiene turnos registrados para ese día");
        }

        listaTurnos.forEach(turno -> {
            turno.setDiaActivo(activo);
            if (!activo) {
                // Si desactivamos el día → desactivamos turnos
                turno.setVigencia(false);
            }
        });

        disponibilidadMedicoRepository.saveAll(listaTurnos);
    }

    /**
     * Crear masivo: valida solapamientos entre lo nuevo y lo existente, y entre los nuevos mismos.
     */
    @Override
    @Transactional
    public List<DisponibilidadMedicoResponseDTO> crearMasivo(List<DisponibilidadMedicoRequestDTO> listaRequestDTO) {
        if (listaRequestDTO == null || listaRequestDTO.isEmpty()) {
            return new ArrayList<>();
        }

        // asumimos que todos los DTOs corresponden al mismo medico (front debería enviar así)
        Integer idMedico = listaRequestDTO.get(0).getIdMedico();

        // validar existencia del medico una sola vez
        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico no encontrado para las disponibilidades masivas"));

        // Validaciones básicas por cada request
        listaRequestDTO.forEach(dto -> validarHoras(dto.getHoraInicio(), dto.getHoraFin()));

        // 1) Validar solapamiento entre los nuevos mismos (por día)
        listaRequestDTO.stream()
                .collect(Collectors.groupingBy(DisponibilidadMedicoRequestDTO::getDiaSemana))
                .forEach((dia, listaPorDia) -> {
                    for (int i = 0; i < listaPorDia.size(); i++) {
                        for (int j = i + 1; j < listaPorDia.size(); j++) {
                            if (horasSeSolapan(listaPorDia.get(i).getHoraInicio(), listaPorDia.get(i).getHoraFin(),
                                              listaPorDia.get(j).getHoraInicio(), listaPorDia.get(j).getHoraFin())) {
                                throw new IllegalArgumentException("Hay solapamiento entre horarios nuevos para el día: " + dia);
                            }
                        }
                    }
                });

        // 2) Validar solapamiento de nuevos con existentes (por día)
        listaRequestDTO.stream()
                .collect(Collectors.groupingBy(DisponibilidadMedicoRequestDTO::getDiaSemana))
                .forEach((dia, listaPorDia) -> {
            List<DisponibilidadMedico> existentes = disponibilidadMedicoRepository
                .findByMedico_IdMedicoAndDiaSemanaAndVigenciaTrue(idMedico, dia);

                    for (DisponibilidadMedicoRequestDTO nuevo : listaPorDia) {
                        if (tieneSolapamientoConLista(nuevo.getHoraInicio(), nuevo.getHoraFin(), existentes)) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "El horario " + nuevo.getHoraInicio() + "-" + nuevo.getHoraFin() +
                                    " se solapa con un turno existente en " + dia);
                        }
                    }
                });

        // 3) Mapear todos y calcular duración
        List<DisponibilidadMedico> entidades = listaRequestDTO.stream().map(dto -> {
            DisponibilidadMedico ent = disponibilidadMedicoMapper.toEntity(dto);
            int duracion = (int) Duration.between(dto.getHoraInicio(), dto.getHoraFin()).toMinutes();
            ent.setDuracionMinutos(duracion);
            // setear el medico ya validado
            ent.setMedico(medico);
            return ent;
        }).collect(Collectors.toList());

        // 4) Guardar todo en una sola transacción
        List<DisponibilidadMedico> guardados = disponibilidadMedicoRepository.saveAll(entidades);

        return disponibilidadMedicoMapper.toDTOList(guardados);
    }

    // ------------------ Cálculos de horas ------------------

    @Override
    public int calcularMinutosPorDia(Integer idMedico, String diaSemana) {
        List<DisponibilidadMedico> lista = disponibilidadMedicoRepository.findByMedico_IdMedicoAndDiaSemanaAndVigenciaTrue(idMedico, diaSemana);
        return lista.stream()
                .filter(d -> Boolean.TRUE.equals(d.getDiaActivo())) // solo días activos
                .mapToInt(d -> d.getDuracionMinutos() == null ? 0 : d.getDuracionMinutos())
                .sum();
    }

    @Override
    public int calcularMinutosSemana(Integer idMedico) {
        List<DisponibilidadMedico> lista = disponibilidadMedicoRepository.findByMedico_IdMedicoAndVigenciaTrue(idMedico);
        return lista.stream()
                .filter(d -> Boolean.TRUE.equals(d.getDiaActivo()))
                .mapToInt(d -> d.getDuracionMinutos() == null ? 0 : d.getDuracionMinutos())
                .sum();
    }

    @Override
    public int calcularMinutosMes(Integer idMedico) {
        // aproximación: minutosSemana * SEMANAS_POR_MES
        double minutosSemana = (double) calcularMinutosSemana(idMedico);
        return (int) Math.round(minutosSemana * SEMANAS_POR_MES);
    }

    // ------------------ Helpers ------------------

    private void validarHoras(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Horas de inicio/fin no pueden ser nulas");
        }
        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La hora fin debe ser mayor a la hora inicio");
        }
    }

    private boolean horasSeSolapan(LocalTime aInicio, LocalTime aFin, LocalTime bInicio, LocalTime bFin) {
        // Dos intervalos [aInicio,aFin) y [bInicio,bFin) se solapan si:
        // aInicio < bFin && bInicio < aFin
        return aInicio.isBefore(bFin) && bInicio.isBefore(aFin);
    }

    private boolean tieneSolapamientoConLista(LocalTime inicio, LocalTime fin, List<DisponibilidadMedico> existentes) {
        if (existentes == null || existentes.isEmpty()) {
            return false;
        }
        for (DisponibilidadMedico ex : existentes) {
            if (Boolean.FALSE.equals(ex.getVigencia())) continue; // sólo vigentes
            if (horasSeSolapan(inicio, fin, ex.getHoraInicio(), ex.getHoraFin())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SlotsDisponibilidadResponseDTO obtenerSlotsDisponibles(Integer idDisponibilidad, LocalDate fecha) {
        DisponibilidadMedico disponibilidad = disponibilidadMedicoRepository.findById(idDisponibilidad)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Disponibilidad no encontrada"));

        // Si el día no está activo o la disponibilidad no está vigente, devolvemos listas vacías
        if (!Boolean.TRUE.equals(disponibilidad.getVigencia()) || !Boolean.TRUE.equals(disponibilidad.getDiaActivo())) {
            return SlotsDisponibilidadResponseDTO.builder()
                    .idDisponibilidad(idDisponibilidad)
                    .fecha(fecha)
                    .slotsDisponibles(Collections.emptyList())
                    .slotsOcupados(Collections.emptyList())
                    .build();
        }

        Integer duracion = disponibilidad.getDuracionMinutos() == null ? 0 : disponibilidad.getDuracionMinutos();
        if (duracion <= 0) {
            // si no hay duración definida, tomamos 30 minutos por defecto
            duracion = 30;
        }

        List<java.time.LocalTime> todosSlots = new ArrayList<>();
        java.time.LocalTime cur = disponibilidad.getHoraInicio();
        while (!cur.plusMinutes(duracion).isAfter(disponibilidad.getHoraFin())) {
            todosSlots.add(cur);
            cur = cur.plusMinutes(duracion);
        }

        // obtener citas ya reservadas para esa disponibilidad y fecha
        List<Cita> citas = citaRepository.findByDisponibilidad_IdDisponibilidadAndFechaCita(idDisponibilidad, fecha);
        Set<String> ocupados = new HashSet<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (Cita c : citas) {
            if (c.getHoraCita() != null) ocupados.add(c.getHoraCita().format(fmt));
        }

        List<String> slotsDisponibles = todosSlots.stream()
                .map(t -> t.format(fmt))
                .filter(s -> !ocupados.contains(s))
                .collect(Collectors.toList());

        List<String> slotsOcupados = todosSlots.stream()
                .map(t -> t.format(fmt))
                .filter(ocupados::contains)
                .collect(Collectors.toList());

        return SlotsDisponibilidadResponseDTO.builder()
                .idDisponibilidad(idDisponibilidad)
                .fecha(fecha)
                .slotsDisponibles(slotsDisponibles)
                .slotsOcupados(slotsOcupados)
                .build();
    }

    @Override
    public List<DisponibilidadMedicoResponseDTO> obtenerDisponibilidadPorMedico(Integer idMedico) {

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("El médico no existe"));

        List<DisponibilidadMedico> lista =
                disponibilidadMedicoRepository.findByMedico_IdMedico(idMedico);

        return lista.stream()
                .map(disponibilidadMedicoMapper::toDTO)
                .toList();
    }
}
