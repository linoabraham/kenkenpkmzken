package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarLoteDTO {
    private Long clienteId;
    private Long servicioId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private Long usuarioAsignadorId;
}