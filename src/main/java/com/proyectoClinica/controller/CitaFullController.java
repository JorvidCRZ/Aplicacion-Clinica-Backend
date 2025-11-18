package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.CitaFullRequestDTO;
import com.proyectoClinica.dto.request.CitaRequestDTO;
import com.proyectoClinica.dto.request.DetalleCitaRequestDTO;
import com.proyectoClinica.dto.request.PacienteRequestDTO;
import com.proyectoClinica.dto.response.CitaResponseDTO;
import com.proyectoClinica.dto.response.DetalleCitaResponseDTO;
import com.proyectoClinica.dto.response.PacienteResponseDTO;
import com.proyectoClinica.service.CitaService;
import com.proyectoClinica.service.DetalleCitaService;
import com.proyectoClinica.service.PacienteService;
import com.proyectoClinica.service.PagoService;
import com.proyectoClinica.service.PagoDetalleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaFullController {

    private final PacienteService pacienteService;
    private final DetalleCitaService detalleCitaService;
    private final CitaService citaService;
    private final PagoService pagoService;
    private final PagoDetalleService pagoDetalleService;

    @PostMapping("/full")
    @Transactional
    public ResponseEntity<CitaResponseDTO> crearFull(@Valid @RequestBody CitaFullRequestDTO request) {
        // 1) Crear paciente si viene
        Integer idPaciente = null;
        PacienteRequestDTO pacienteReq = request.getPaciente();
        if (pacienteReq != null) {
            PacienteResponseDTO pacienteResp = pacienteService.crear(pacienteReq);
            idPaciente = pacienteResp.getIdPaciente();
        }

        // 2) Crear detalleCita si viene
        Integer idDetalle = null;
        DetalleCitaRequestDTO detalleReq = request.getDetalleCita();
        if (detalleReq != null) {
            DetalleCitaResponseDTO detalleResp = detalleCitaService.crear(detalleReq);
            idDetalle = detalleResp.getIdDetalleCita();
        }

        // 3) Preparar CitaRequestDTO final
        CitaRequestDTO citaReq = request.getCita();
        if (citaReq == null) {
            throw new IllegalArgumentException("El objeto 'cita' es obligatorio en el request");
        }
        // si no vino idPaciente en request.cita, tomar el paciente creado
        if ((citaReq.getIdPaciente() == null || citaReq.getIdPaciente() <= 0) && idPaciente != null) {
            citaReq.setIdPaciente(idPaciente);
        }
        // si no vino idDetalleCita en request.cita, tomar el detalle creado
        if ((citaReq.getIdDetalleCita() == null || citaReq.getIdDetalleCita() <= 0) && idDetalle != null) {
            citaReq.setIdDetalleCita(idDetalle);
        }

        CitaResponseDTO created = citaService.crear(citaReq);

        // 4) Si llega objeto pago, crearlo y luego los pagoDetalles vinculados a la cita creada
        if (request.getPago() != null) {
            com.proyectoClinica.dto.request.PagoRequestDTO pagoReq = request.getPago();
            com.proyectoClinica.dto.response.PagoResponseDTO pagoResp = pagoService.crear(pagoReq);

            // Si hay detalles de pago, crear cada uno asociando idCita y idPago
            if (request.getPagoDetalles() != null && !request.getPagoDetalles().isEmpty()) {
                for (com.proyectoClinica.dto.request.PagoDetalleRequestDTO pdReq : request.getPagoDetalles()) {
                    // asegurar que el idCita apunte a la cita creada
                    pdReq.setIdCita(created.getIdCita());
                    pdReq.setIdPago(pagoResp.getIdPago());
                    pagoDetalleService.crear(pdReq);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
