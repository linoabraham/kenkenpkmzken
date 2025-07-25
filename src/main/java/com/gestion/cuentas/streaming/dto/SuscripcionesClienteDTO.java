package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.util.List;

@Data
public class SuscripcionesClienteDTO {
    private Long clienteId;
    private String nombreCliente;
    private String numeroCliente;
    private List<CuentaAsignadaDTO> cuentasCompletas;
    private List<PerfilDTO> perfilesIndividuales;
}