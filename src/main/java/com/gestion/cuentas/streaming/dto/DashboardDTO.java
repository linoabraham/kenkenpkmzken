package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long cuentasActivas;
    private Long clientesActivos;
    private Map<String, Long> cuentasPorServicio;
    private Map<String, Long> serviciosMasVendidos;
}