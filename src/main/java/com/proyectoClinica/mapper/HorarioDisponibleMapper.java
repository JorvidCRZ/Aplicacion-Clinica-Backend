package com.proyectoClinica.mapper;

import com.proyectoClinica.dto.response.HorarioPorFechaResponseDTO;
import com.proyectoClinica.dto.response.HorariosMedicoResponseDTO;
import com.proyectoClinica.model.HorarioBloque;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HorarioDisponibleMapper {

    public HorariosMedicoResponseDTO toHorariosMedicoResponse(Integer idMedico, List<HorarioBloque> bloques) {

        Map<LocalDate, List<HorarioBloque>> grup = bloques.stream()
                .collect(Collectors.groupingBy(HorarioBloque::getFecha));

        List<HorarioPorFechaResponseDTO> dias = grup.entrySet().stream()
                .map(e -> {

                    LocalDate fecha = e.getKey();
                    List<HorarioBloque> lista = e.getValue();

                    List<String> horarios = lista.stream()
                            .sorted(Comparator.comparing(HorarioBloque::getHoraInicio))
                            .map(h -> h.getHoraInicio().toString().substring(0, 5))
                            .toList();

                    return new HorarioPorFechaResponseDTO(
                            fecha.toString(),
                            fecha.getDayOfWeek()
                                    .getDisplayName(TextStyle.FULL, new Locale("es"))
                                    .toUpperCase(),
                            horarios
                    );
                })
                .sorted(Comparator.comparing(HorarioPorFechaResponseDTO::getFecha))
                .toList();

        return new HorariosMedicoResponseDTO(idMedico, dias);
    }
}
