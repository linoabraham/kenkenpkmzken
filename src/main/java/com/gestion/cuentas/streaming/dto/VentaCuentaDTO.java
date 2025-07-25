package com.gestion.cuentas.streaming.dto;

import com.gestion.cuentas.streaming.enums.TipoCliente;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VentaCuentaDTO {
    private Long id;
    private Long cuentaId;
    private Long clienteId;
    private Long perfilId;
    private String nombreServicio; // Campo útil para frontend
    private String urlImg;         // ✅ CAMPO AÑADIDO
    private BigDecimal precioVenta;
    private LocalDateTime fechaVenta;
    private TipoCliente tipoCliente;
    private Long usuarioAsignadorId;
}