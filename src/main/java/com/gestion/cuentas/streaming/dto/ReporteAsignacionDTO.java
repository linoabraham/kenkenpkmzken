package com.gestion.cuentas.streaming.dto;

import com.gestion.cuentas.streaming.enums.TipoCuenta;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReporteAsignacionDTO {
    private Long cuentaId;
    private String correoCuenta;
    private TipoCuenta tipoCuenta; // "COMPLETO" o "INDIVIDUAL"

    // --- Campos para cuentas de tipo COMPLETO ---
    private Long clienteAsignadoId;
    private String nombreClienteAsignado;
    private LocalDate fechaInicioCuenta;
    private LocalDate fechaRenovacionCuenta;

    // --- Campos para cuentas de tipo INDIVIDUAL ---
    private Integer perfilesMaximos;
    private List<PerfilDTO> perfilesAsignados;
}