package com.gestion.cuentas.streaming.dto;

import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Long id;
    private String correo;
    private String contraseña;
    private String pin;
    private Integer perfilesMaximos;
    private int perfilesOcupados;
    private String enlace;
    private LocalDate fechaInicio;
    private LocalDate fechaRenovacion;
    private StatusCuenta status;
    private TipoCuenta tipoCuenta;
    private BigDecimal precioVenta;
    private Long clienteId;
    private Long servicioId;
}