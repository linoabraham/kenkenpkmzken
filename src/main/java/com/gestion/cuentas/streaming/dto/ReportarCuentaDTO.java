package com.gestion.cuentas.streaming.dto;

import lombok.Data;

@Data
public class ReportarCuentaDTO {
    private Long usuarioId;
    private String motivo;
    private String detalle;

    // ✅ CAMPO AÑADIDO:
    // true = El reporte es grave, la cuenta debe pasar a VENCIDO.
    // false = El reporte es solo una nota, la cuenta sigue ACTIVA.
    private boolean marcarComoVencida;
}