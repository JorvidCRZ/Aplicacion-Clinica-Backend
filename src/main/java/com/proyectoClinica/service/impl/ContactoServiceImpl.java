package com.proyectoClinica.service.impl;

import com.proyectoClinica.dto.request.ContactoRequestDTO;
import com.proyectoClinica.dto.response.ContactoResponseDTO;
import com.proyectoClinica.mapper.ContactoMapper;
import com.proyectoClinica.model.Contacto;
import com.proyectoClinica.model.Usuario;
import com.proyectoClinica.repository.ContactoRepository;
import com.proyectoClinica.repository.UsuarioRepository;
import com.proyectoClinica.service.ContactoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoServiceImpl implements ContactoService {

    private final ContactoRepository contactoRepository;
    private final ContactoMapper contactoMapper;
    private final JavaMailSender mailSender;
    private final UsuarioRepository usuarioRepository;

    @Value("${clinica.contacto.email}")
    private String clinicEmail;

    @Override
    @Transactional
    public ContactoResponseDTO crear(ContactoRequestDTO requestDTO) {
        Contacto contacto = contactoMapper.toEntity(requestDTO);
        // If the DTO includes an idUsuario, try to attach the Usuario
        if (requestDTO.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(requestDTO.getIdUsuario()).orElse(null);
            contacto.setUsuario(usuario);
        }

        Contacto guardado = contactoRepository.save(contacto);

        // Attempt to send notification email; failure shouldn't prevent response
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(clinicEmail);
            message.setReplyTo(guardado.getCorreo());
            message.setSubject("Nuevo contacto desde formulario: " + guardado.getNombre());
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(guardado.getNombre()).append("\n");
            sb.append("Correo: ").append(guardado.getCorreo()).append("\n\n");
            sb.append("Teléfono: ").append(guardado.getTelefono()).append("\n");
            sb.append("Tipo de consulta: ").append(guardado.getTipoConsulta()).append("\n");
            sb.append("Asunto: ").append(guardado.getAsunto()).append("\n\n");
            sb.append("Mensaje:\n").append(guardado.getMensaje()).append("\n\n");
            if (guardado.getUsuario() != null) {
                sb.append("Usuario ID: ").append(guardado.getUsuario().getIdUsuario()).append("\n");
            }
            sb.append("Fecha: ").append(guardado.getFecha()).append("\n");

            message.setText(sb.toString());
            mailSender.send(message);
        } catch (Exception e) {
            // Log but don't fail the save (project uses no logger here; rethrowing would break contract)
            System.err.println("No se pudo enviar el email de contacto: " + e.getMessage());
        }

        return contactoMapper.toDTO(guardado);
    }

    @Override
    public ContactoResponseDTO obtenerPorId(Integer id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
        return contactoMapper.toDTO(contacto);
    }

    @Override
    public List<ContactoResponseDTO> listar() {
        return contactoMapper.toDTOList(contactoRepository.findAll());
    }
}
