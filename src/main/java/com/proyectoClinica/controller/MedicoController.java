package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.ActualizarPerfilMedicoRequestDTO;
import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.*;
import com.proyectoClinica.service.DisponibilidadMedicoService;
import com.proyectoClinica.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listar(){
        return ResponseEntity.ok(medicoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> obtenerPorId(@PathVariable Integer id){
        return ResponseEntity.ok(medicoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> crear(@Valid @RequestBody MedicoRequestDTO requestDTO){
        return ResponseEntity.ok(medicoService.crear(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/perfil")
    public ResponseEntity<Map<String,Object>> obtenerPerfil(@PathVariable Integer id) {
        MedicoResponseDTO dto = medicoService.obtenerPorId(id);
        Map<String,Object> body = new HashMap<>();
        body.put("mensaje", "Perfil médico");
        body.put("data", dto);
        return ResponseEntity.ok(body);
    }
    @GetMapping("/detalle")
    public ResponseEntity<List<MedicoListadoResponseDTO>> listarDetalle() {
        return ResponseEntity.ok(medicoService.listarMedicosDetalle());
    }


    // ejemplo: endpoint para las citas del medico (implementa MedicoService)
    /*@GetMapping("/{id}/citas")
    public ResponseEntity<Map<String,Object>> citasMedico(@PathVariable Integer id) {
        Map<String,Object> body = new HashMap<>();
        body.put("mensaje", "Citas del médico");
        body.put("data", medicoService.listarCitas(id));
        return ResponseEntity.ok(body);
    }*/


    private final DisponibilidadMedicoService disponibilidadMedicoService;


    @GetMapping("/{idMedico}/disponibilidad")
    public ResponseEntity<List<DisponibilidadMedicoResponseDTO>> obtenerDisponibilidadPorMedico(
            @PathVariable Integer idMedico) {

        List<DisponibilidadMedicoResponseDTO> disponibilidad =
                disponibilidadMedicoService.obtenerDisponibilidadPorMedico(idMedico);

        return ResponseEntity.ok(disponibilidad);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<MedicoResponseDTO> obtenerMedicoPorUsuario(@PathVariable Integer idUsuario) {
        MedicoResponseDTO dto = medicoService.obtenerPorUsuario(idUsuario);
        return ResponseEntity.ok(dto);
    }

    // DELETE para eliminar un horario de disponibilidad por su ID
    @DeleteMapping("/disponibilidad/{idDisponibilidad}")
    public ResponseEntity<Void> eliminarDisponibilidad(@PathVariable Integer idDisponibilidad) {
        disponibilidadMedicoService.eliminar(idDisponibilidad);
        return ResponseEntity.noContent().build();
    }


    //
    @GetMapping("/perfil/{idMedico}")
    public ResponseEntity<List<PerfilMedicoDTO>> listarperfilpormedico( @PathVariable Integer idMedico) {
        return ResponseEntity.ok(medicoService.listarPerfilDashboardPorMedico(idMedico));
    }

    //
    @PutMapping("/perfil/actualizar/{idMedico}")
    public ResponseEntity<PerfilMedicoDTO> actualizarPerfil(
            @PathVariable Integer idMedico,
            @RequestBody ActualizarPerfilMedicoRequestDTO request
    ){
        return ResponseEntity.ok(
                medicoService.actualizarPerfil(idMedico, request)
        );
    }


}
