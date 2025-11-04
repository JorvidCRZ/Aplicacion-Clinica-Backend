package com.proyectoClinica.controller;

import com.proyectoClinica.dto.request.MedicoRequestDTO;
import com.proyectoClinica.dto.response.MedicoListadoResponseDTO;
import com.proyectoClinica.dto.response.MedicoResponseDTO;
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
}
