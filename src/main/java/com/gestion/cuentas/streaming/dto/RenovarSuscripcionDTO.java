package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RenovarSuscripcionDTO {
    private BigDecimal nuevoPrecioVenta;
    private Long usuarioId; // El ID del usuario que registra la renovación
}