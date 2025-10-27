package com.proyectoClinica.scheduler;

import com.proyectoClinica.service.RecordatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class RecordatorioScheduler {

    private static final Logger log = LoggerFactory.getLogger(RecordatorioScheduler.class);

    private final RecordatorioService recordatorioService;

    // Ejecutar cada minuto y enviar recordatorios pendientes cuya fecha_envio <= now
    @Scheduled(fixedDelay = 60000)
    public void enviarPendientes() {
        try {
            recordatorioService.enviarPendientes();
        } catch (Exception ex) {
            log.error("Error en RecordatorioScheduler: {}", ex.getMessage(), ex);
        }
    }
}
