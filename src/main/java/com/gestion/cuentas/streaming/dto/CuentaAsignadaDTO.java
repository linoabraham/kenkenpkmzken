package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CuentaAsignadaDTO {
    private Long cuentaId;
    private String correo;
    private String nombreServicio;
    private String urlImgServicio;
    private LocalDate fechaInicio;
    private LocalDate fechaRenovacion;
    private String status;
}