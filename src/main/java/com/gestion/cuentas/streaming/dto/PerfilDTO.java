package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PerfilDTO {
    private Long id;
    private String nombrePerfil;
    private Long clienteId;
    private String correoCuenta; // Será nulo si el perfil está libre
    private String nombreCliente;
    private String urlImg;// Campo útil para mostrar en el frontend para ver el servicio url
    private String numero;//cliente su numero
    private String contraseña;
    private String pin;
    private LocalDate fechaInicio;
    private LocalDate fechaRenovacion;
    private BigDecimal precioVenta;
}