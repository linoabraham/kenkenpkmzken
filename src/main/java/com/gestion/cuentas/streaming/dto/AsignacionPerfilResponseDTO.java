package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.util.List;

@Data
public class AsignacionPerfilResponseDTO {
    private Long cuentaId;
    private String correoCuenta;
    private String statusCuenta;
    private List<PerfilDTO> perfilesAsignadosExitosamente;
}