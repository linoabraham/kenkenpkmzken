package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistorialCuentaDTO {
    private Long id;
    private Long cuentaAnteriorId;
    private Long cuentaNuevaId;
    private String motivo;
    private LocalDateTime fechaCambio;
    private Long usuarioId;
}