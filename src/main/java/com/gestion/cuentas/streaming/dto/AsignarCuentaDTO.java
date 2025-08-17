package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarCuentaDTO {
    private Long cuentaId;
    private Long clienteId;
    private BigDecimal precioVenta;
    private Long usuarioAsignadorId;

    // ✅ CAMPOS AÑADIDOS
    private LocalDate fechaInicio;
    private LocalDate fechaRenovacion;
}