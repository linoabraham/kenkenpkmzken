package com.gestion.cuentas.streaming.dto;

import lombok.Data;

@Data
public class ReemplazarCuentaDTO {
    private Long cuentaNuevaId;
    private Long usuarioId; // Quien hace el reemplazo
    private String motivo;  // Razón del reemplazo
}