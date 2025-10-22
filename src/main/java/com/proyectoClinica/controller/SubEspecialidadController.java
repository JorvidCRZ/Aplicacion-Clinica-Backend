package com.proyectoClinica.controller;

import com.proyectoClinica.service.SubEspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sub-especialidades")
@RequiredArgsConstructor
public class SubEspecialidadController {

    private final SubEspecialidadService subEspecialidadService;
}
