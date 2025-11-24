package com.proyectoClinica.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.proyectoClinica.repository.CitaRepository;

@Component
public class CitaScheduler {

    @Autowired
    private CitaRepository citaRepository;

    // Ejecuta cada 1 minuto
    @Scheduled(fixedRate = 10000)
    public void actualizarCitasNoShow() {
        citaRepository.updateCitasNoShow();
        citaRepository.revertirNoShowSiCitaSeMovio(); // Revierte a programada
    }
}

