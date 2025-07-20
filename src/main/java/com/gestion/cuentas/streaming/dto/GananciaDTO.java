package com.gestion.cuentas.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GananciaDTO {
    private LocalDate fecha;
    private Integer totalVentas;
    private BigDecimal ganancia;
}