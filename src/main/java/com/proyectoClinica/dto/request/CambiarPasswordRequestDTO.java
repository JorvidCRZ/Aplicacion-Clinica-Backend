package com.proyectoClinica.dto.request;

import lombok.Data;

@Data
public class CambiarPasswordRequestDTO {

    private String actual;
    private String nueva;

}
