package com.proyectoClinica.service.impl;
import com.proyectoClinica.dto.request.HorarioBloqueRequestDTO;
import com.proyectoClinica.dto.response.BloquesPorDiaResponseDTO;
import com.proyectoClinica.dto.response.DisponibilidadDashboardResponse;
import com.proyectoClinica.dto.response.HorarioBloqueResponseDTO;
import com.proyectoClinica.dto.response.HorariosMedicoResponseDTO;
import com.proyectoClinica.mapper.HorarioBloqueMapper;
import com.proyectoClinica.mapper.HorarioDisponibleMapper;
import com.proyectoClinica.model.DisponibilidadMedico;
import com.proyectoClinica.model.HorarioBloque;
import com.proyectoClinica.repository.HorarioBloqueRepository;
import com.proyectoClinica.service.HorarioBloqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioBloqueServiceImpl implements HorarioBloqueService{

    private final HorarioBloqueRepository repository;
    private final HorarioBloqueMapper mapper;
    private final HorarioDisponibleMapper horarioDisponibleMapper;


    @Override
    public HorarioBloqueResponseDTO crear(HorarioBloqueRequestDTO request) {
        // Mapea el DTO a la entidad
        HorarioBloque entity = mapper.toEntity(request);

        // 🔹 Obtener la duración desde la disponibilidad
        DisponibilidadMedico disponibilidad = entity.getDisponibilidadMedico();
        if (disponibilidad == null || disponibilidad.getDuracionMinutos() == null) {
            throw new RuntimeException("No se encontró duración de la disponibilidad");
        }

        // 🔹 Calcular horaFin automáticamente usando la duración
        entity.setHoraFin(entity.getHoraInicio().plusMinutes(disponibilidad.getDuracionMinutos()));

        // Guarda el bloque
        HorarioBloque guardado = repository.save(entity);

        // Devuelve DTO
        return mapper.toResponse(guardado);
    }

    @Override
    public List<HorarioBloqueResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HorarioBloqueResponseDTO obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));
    }

    @Override
    public HorarioBloqueResponseDTO actualizar(Integer id, HorarioBloqueRequestDTO request) {
        HorarioBloque entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));
        HorarioBloque actualizado = mapper.toEntity(request);
        actualizado.setIdBloque(entity.getIdBloque());
        return mapper.toResponse(repository.save(actualizado));
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public HorariosMedicoResponseDTO obtenerHorariosPorMedico(Integer idMedico) {
        List<HorarioBloque> bloques = repository.findHorariosDisponiblesPorMedico(idMedico);
        return horarioDisponibleMapper.toHorariosMedicoResponse(idMedico, bloques);
    }


//
//    @Override
//    public List<HorarioBloque> obtenerBloquesDisponibles(Integer idMedicoEspecialidad, LocalDate fecha) {
//        return List.of();
//    }

    @Override
    public List<DisponibilidadDashboardResponse> listarDisponibilidades() {

        List<Object[]> rows = repository.listarDisponibilidadesAgrupadas();
        List<DisponibilidadDashboardResponse> list = new ArrayList<>();

        for (Object[] r : rows) {
            DisponibilidadDashboardResponse d = new DisponibilidadDashboardResponse();

            d.setId((Integer) r[0]);
            d.setMedico((String) r[1]);
            d.setEspecialidad((String) r[2]);
            d.setDias((String) r[3]);
            d.setHoraInicio(r[4].toString());
            d.setHoraFin(r[5].toString());
            d.setDuracion((Integer) r[6]); // ya casteado en SQL
            d.setBloques((Integer) r[7]);  // ya casteado en SQL
            d.setEstado((String) r[8]);

            list.add(d);
        }

        return list;
    }


    @Override
    public List<BloquesPorDiaResponseDTO> obtenerBloquesPorMedico(Integer idMedico) {
        List<HorarioBloque> bloques = repository.findByMedicoIdOrderByFechaHora(idMedico);

        // Agrupar por fecha
        Map<LocalDate, List<HorarioBloque>> bloquesPorFecha = bloques.stream()
                .collect(Collectors.groupingBy(HorarioBloque::getFecha, LinkedHashMap::new, Collectors.toList()));

        List<BloquesPorDiaResponseDTO> response = new ArrayList<>();

        for (Map.Entry<LocalDate, List<HorarioBloque>> entry : bloquesPorFecha.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<HorarioBloque> listaBloques = entry.getValue();

            BloquesPorDiaResponseDTO dto = new BloquesPorDiaResponseDTO();
            dto.setFecha(fecha);
            dto.setDia(nombreDiaEnEspannol(fecha));

            List<BloquesPorDiaResponseDTO.BloqueDTO> bloquesDto = listaBloques.stream().map(b -> {
                BloquesPorDiaResponseDTO.BloqueDTO bDto = new BloquesPorDiaResponseDTO.BloqueDTO();
                bDto.setHoraInicio(b.getHoraInicio());
                bDto.setHoraFin(b.getHoraFin());
                return bDto;
            }).toList();

            dto.setBloques(bloquesDto);
            response.add(dto);
        }

        return response;
    }

    private String nombreDiaEnEspannol(LocalDate fecha) {
        switch (fecha.getDayOfWeek()) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }

    @Override
    public int actualizarDisponibilidadPorMedico(Integer idMedico, boolean disponible) {
        return repository.actualizarDisponibilidadPorMedico(idMedico, disponible);
    }





}
