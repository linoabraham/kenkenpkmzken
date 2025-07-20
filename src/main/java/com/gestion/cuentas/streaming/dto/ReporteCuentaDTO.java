package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCuentaDTO {
    private Long id;
    private Long cuentaId;
    private Long usuarioId;
    private LocalDate fecha;
    private String motivo;
    private String detalle;
}