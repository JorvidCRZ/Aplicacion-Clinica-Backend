package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.PagoDetalleRequestDTO;
import com.proyectoClinica.dto.response.PagoDetalleResponseDTO;
import com.proyectoClinica.mapper.PagoDetalleMapper;
import com.proyectoClinica.model.PagoDetalle;
import com.proyectoClinica.repository.PagoDetalleRepository;
import com.proyectoClinica.service.PagoDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleServiceImpl implements PagoDetalleService {

    private final PagoDetalleRepository pagoDetalleRepository;
    private final PagoDetalleMapper pagoDetalleMapper;
    private final com.proyectoClinica.service.RecordatorioService recordatorioService;
    private final com.proyectoClinica.repository.UsuarioRepository usuarioRepository;
    private final com.proyectoClinica.service.impl.RecordatorioSender recordatorioSender;
    private final com.proyectoClinica.repository.PagoRepository pagoRepository;
    private final com.proyectoClinica.repository.CitaRepository citaRepository;

    private static final Logger log = LoggerFactory.getLogger(PagoDetalleServiceImpl.class);

    @Override
    public PagoDetalleResponseDTO crear(PagoDetalleRequestDTO requestDTO) {
        PagoDetalle pagoDetalle = pagoDetalleMapper.toEntity(requestDTO);

        // Asegurar que las referencias a Pago y Cita sean entidades gestionadas por JPA
        try {
            if (requestDTO.getIdPago() != null) {
                com.proyectoClinica.model.Pago pago = pagoRepository.findById(requestDTO.getIdPago())
                        .orElseThrow(() -> new RuntimeException("Pago no encontrado id=" + requestDTO.getIdPago()));
                pagoDetalle.setPago(pago);
            }
        } catch (Exception ex) {
            // si no encontramos el pago dejamos que la excepción se propague más abajo al intentar guardar
            log.warn("No se pudo resolver Pago antes de guardar PagoDetalle: {}", ex.getMessage());
        }

        try {
            if (requestDTO.getIdCita() != null) {
                com.proyectoClinica.model.Cita cita = citaRepository.findById(requestDTO.getIdCita())
                        .orElseThrow(() -> new RuntimeException("Cita no encontrada id=" + requestDTO.getIdCita()));
                pagoDetalle.setCita(cita);
            }
        } catch (Exception ex) {
            log.warn("No se pudo resolver Cita antes de guardar PagoDetalle: {}", ex.getMessage());
        }

        PagoDetalle guardado = pagoDetalleRepository.save(pagoDetalle);

        // Después de guardar el pago detalle, crear recordatorios solo si el pago está en estado PAGADO
        try {
            String estadoPago = null;
            if (guardado.getPago() != null) {
                try {
                    estadoPago = guardado.getPago().getEstado();
                } catch (Exception ex) {
                    // en caso de proxy lazy o problema, lo ignoramos y dejamos estadoPago null
                    log.warn("No se pudo leer estado del pago asociado: {}", ex.getMessage());
                }
            }

            if (estadoPago != null && "PAGADO".equalsIgnoreCase(estadoPago)) {
                // 1) inmediato (confirmación tras pago) — solo si la devolución tiene una cita asociada
                try {
                    if (guardado.getCita() != null && guardado.getCita().getIdCita() != null) {
                        String destinatario = null;
                        try {
                            if (guardado.getCita().getPaciente() != null && guardado.getCita().getPaciente().getPersona() != null) {
                                Integer idPersona = guardado.getCita().getPaciente().getPersona().getIdPersona();
                                if (idPersona != null) {
                                    java.util.Optional<com.proyectoClinica.model.Usuario> u = usuarioRepository.findByPersona_IdPersona(idPersona);
                                    if (u.isPresent()) destinatario = u.get().getCorreo();
                                }
                            }
                        } catch (Exception ex) {
                            log.warn("No se pudo resolver destinatario desde usuario: {}", ex.getMessage());
                        }

                        com.proyectoClinica.dto.request.RecordatorioRequestDTO inmediato = com.proyectoClinica.dto.request.RecordatorioRequestDTO.builder()
                                .idCita(guardado.getCita().getIdCita())
                                .fechaEnvio(java.time.LocalDateTime.now())
                                .tipo("PAGO_CONFIRMACION")
                                .estado("PENDIENTE")
                                .destinatarioCorreo(destinatario)
                                .build();
                        com.proyectoClinica.dto.response.RecordatorioResponseDTO creado = recordatorioService.crear(inmediato);
                        // Enviar de forma síncrona para que la API responda después del intento de envío
                        try {
                            if (creado != null && creado.getIdRecordatorio() != null) {
                                String resultado = recordatorioSender.enviarSync(creado.getIdRecordatorio());
                                log.info("Envio sincronico recordatorio {} resultado: {}", creado.getIdRecordatorio(), resultado);
                            }
                        } catch (Exception ex) {
                            log.error("Error enviando sincronamente recordatorio {}: {}", (creado != null ? creado.getIdRecordatorio() : null), ex.getMessage(), ex);
                        }
                    } else {
                        log.warn("PagoDetalle guardado pero sin cita asociada; no se crea recordatorio inmediato.");
                    }
                } catch (Exception e) {
                    log.error("No se pudo crear/enviar recordatorio inmediato: {}", e.getMessage(), e);
                }

                // 2) programado 3 días antes de la cita
                try {
                    if (guardado.getCita() != null && guardado.getCita().getFechaCita() != null && guardado.getCita().getHoraCita() != null) {
                        // Programar para la misma hora de la cita, 3 días antes
                        java.time.LocalDateTime fechaProgramada = guardado.getCita().getFechaCita().atTime(guardado.getCita().getHoraCita()).minusDays(3);
                        String destinatarioProgramado = null;
                        try {
                            if (guardado.getCita().getPaciente() != null && guardado.getCita().getPaciente().getPersona() != null) {
                                Integer idPersona = guardado.getCita().getPaciente().getPersona().getIdPersona();
                                if (idPersona != null) {
                                    java.util.Optional<com.proyectoClinica.model.Usuario> u2 = usuarioRepository.findByPersona_IdPersona(idPersona);
                                    if (u2.isPresent()) destinatarioProgramado = u2.get().getCorreo();
                                }
                            }
                        } catch (Exception ex) {
                            log.warn("No se pudo resolver destinatario programado desde usuario: {}", ex.getMessage());
                        }

                        com.proyectoClinica.dto.request.RecordatorioRequestDTO programado = com.proyectoClinica.dto.request.RecordatorioRequestDTO.builder()
                                .idCita(guardado.getCita().getIdCita())
                                .fechaEnvio(fechaProgramada)
                                .tipo("PREVIO_3_DIAS")
                                .estado("PENDIENTE")
                                .destinatarioCorreo(destinatarioProgramado)
                                .build();
                        recordatorioService.crear(programado);
                    }
                } catch (Exception e) {
                    log.error("No se pudo crear recordatorio programado: {}", e.getMessage(), e);
                }
            } else {
                log.info("PagoDetalle creado pero el pago asociado no está en estado PAGADO (estado={}), no se crearán recordatorios.", estadoPago);
            }
        } catch (Exception e) {
            log.error("Error comprobando estado del pago para crear recordatorios: {}", e.getMessage(), e);
        }

        return pagoDetalleMapper.toDTO(guardado);
    }

    @Override
    public PagoDetalleResponseDTO obtenerPorId(Integer id) {
        PagoDetalle pagoDetalle = pagoDetalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago detalle no encontrado"));
        return pagoDetalleMapper.toDTO(pagoDetalle);
    }

    @Override
    public List<PagoDetalleResponseDTO> listar() {
        return pagoDetalleMapper.toDTOList(pagoDetalleRepository.findAll());
    }

    @Override
    public void eliminar(Integer id) {
        pagoDetalleRepository.deleteById(id);
    }
}
