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
    private BigDecimal precioVenta;
    private LocalDateTime fechaVenta;
    private TipoCliente tipoCliente;
    private Long usuarioAsignadorId;

    /**
     * El ID del perfil específico que se vendió.
     * Será nulo si la venta fue de una cuenta de tipo COMPLETO.
     */
    private Long perfilId;
}