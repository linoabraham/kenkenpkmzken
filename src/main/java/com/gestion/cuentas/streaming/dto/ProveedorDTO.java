package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String numero;
    private String linkWhatsapp;
    private String tipoServicio;
    private String tipoCuentaQueProvee;
    private BigDecimal precioReferencial;
}