package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopClienteDTO {
    private Long clienteId;
    private String nombreCliente;
    private String numeroCliente;
    private Long totalCompras;
}