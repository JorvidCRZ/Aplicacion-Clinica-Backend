package com.proyectoClinica.service.impl;

import com.proyectoClinica.model.Recordatorio;
import com.proyectoClinica.repository.RecordatorioRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecordatorioSender {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioSender.class);

    private final JavaMailSender mailSender;
    private final RecordatorioRepository recordatorioRepository;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Async
    public void enviarAsync(Integer idRecordatorio) {
        try {
            Optional<Recordatorio> opt = recordatorioRepository.findById(idRecordatorio);
            if (opt.isEmpty()) {
                log.warn("Recordatorio id {} no encontrado para enviar", idRecordatorio);
                return;
            }
            Recordatorio r = opt.get();

            String destinatario = r.getDestinatarioCorreo();
            if (destinatario == null || destinatario.isBlank()) {
                // si no hay destinatario, no intentamos enviar
                log.warn("Recordatorio id {} no tiene destinatario definido, se omite envio", idRecordatorio);
                r.setEstado("ERROR_ENVIO");
                recordatorioRepository.save(r);
                return;
            }

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(destinatario);
            if (mailFrom != null && !mailFrom.isBlank()) helper.setFrom(mailFrom);
            helper.setSubject("Recordatorio de cita - Clinica TuSalud");

            // Construir HTML sencillo (puedes reemplazar por plantilla Thymeleaf/freemarker)
            StringBuilder html = new StringBuilder();
            html.append("<html><body>");
            html.append("<p>Hola,</p>");
            html.append("<p>Este es un recordatorio de su cita:</p>");
            if (r.getCita() != null) {
                html.append("<ul>");
                html.append("<li><strong>Fecha:</strong> ").append(r.getCita().getFechaCita()).append("</li>");
                html.append("<li><strong>Hora:</strong> ").append(r.getCita().getHoraCita()).append("</li>");
                if (r.getCita().getDetalleCita() != null && r.getCita().getDetalleCita().getMedicoEspecialidad() != null
                        && r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico() != null
                        && r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico().getPersona() != null) {
                    html.append("<li><strong>Médico:</strong> ")
                            .append(r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico().getPersona().getNombre1())
                            .append(" ")
                            .append(r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico().getPersona().getNombre2())
                            .append(" ")
                            .append(r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico().getPersona().getApellidoPaterno())
                            .append(" ")
                            .append(r.getCita().getDetalleCita().getMedicoEspecialidad().getMedico().getPersona().getApellidoMaterno())
                            .append("</li>");
                }
                html.append("<li><strong>Motivo:</strong> ")
                        .append(r.getCita().getMotivoConsulta() == null ? "-" : r.getCita().getMotivoConsulta())
                        .append("</li>");
                html.append("</ul>");
            }
            html.append("<p>Saludos,<br/>Clinica TuSalud</p>");
            html.append("</body></html>");

            helper.setText(html.toString(), true);

            mailSender.send(mime);

            r.setEstado("ENVIADO");
            recordatorioRepository.save(r);
            log.info("Recordatorio id {} enviado a {}", idRecordatorio, destinatario);
        } catch (Exception ex) {
            log.error("Error enviando recordatorio id {}: {}", idRecordatorio, ex.getMessage(), ex);
            try {
                Optional<Recordatorio> opt = recordatorioRepository.findById(idRecordatorio);
                if (opt.isPresent()) {
                    Recordatorio r = opt.get();
                    r.setEstado("ERROR_ENVIO");
                    recordatorioRepository.save(r);
                }
            } catch (Exception e2) {
                log.error("No se pudo actualizar estado ERROR_ENVIO para recordatorio {}: {}", idRecordatorio, e2.getMessage());
            }
        }
    }
    
    /**
     * Envío síncrono para debugging: lanza excepción si falla y devuelve mensaje si tiene éxito.
     */
    public String enviarSync(Integer idRecordatorio) throws Exception {
        Optional<Recordatorio> opt = recordatorioRepository.findById(idRecordatorio);
        if (opt.isEmpty()) {
            String msg = "Recordatorio id " + idRecordatorio + " no encontrado";
            log.warn(msg);
            throw new RuntimeException(msg);
        }

        Recordatorio r = opt.get();
        String destinatario = r.getDestinatarioCorreo();
        if (destinatario == null || destinatario.isBlank()) {
            r.setEstado("ERROR_ENVIO");
            recordatorioRepository.save(r);
            String msg = "Recordatorio id " + idRecordatorio + " no tiene destinatario";
            log.warn(msg);
            throw new RuntimeException(msg);
        }

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setTo(destinatario.trim());
        if (mailFrom != null && !mailFrom.isBlank()) helper.setFrom(mailFrom);
        helper.setSubject("Recordatorio de cita - Clinica TuSalud");

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<p>Hola,</p>");
        html.append("<p>Este es un recordatorio de su cita:</p>");
        if (r.getCita() != null) {
            html.append("<ul>");
            html.append("<li><strong>Fecha:</strong> ").append(r.getCita().getFechaCita()).append("</li>");
            html.append("<li><strong>Hora:</strong> ").append(r.getCita().getHoraCita()).append("</li>");
            html.append("<li><strong>Motivo:</strong> ")
                    .append(r.getCita().getMotivoConsulta() == null ? "-" : r.getCita().getMotivoConsulta())
                    .append("</li>");
            html.append("</ul>");
        }
        html.append("<p>Saludos,<br/>Clinica TuSalud</p>");
        html.append("</body></html>");

        helper.setText(html.toString(), true);

        try {
            mailSender.send(mime);
            r.setEstado("ENVIADO");
            recordatorioRepository.save(r);
            String ok = "Recordatorio id " + idRecordatorio + " enviado a " + destinatario;
            log.info(ok);
            return ok;
        } catch (Exception ex) {
            log.error("Error enviando recordatorio id {}: {}", idRecordatorio, ex.getMessage(), ex);
            try {
                r.setEstado("ERROR_ENVIO");
                recordatorioRepository.save(r);
            } catch (Exception e2) {
                log.error("No se pudo actualizar estado ERROR_ENVIO para recordatorio {}: {}", idRecordatorio, e2.getMessage());
            }
            throw ex;
        }
    }
    
}
