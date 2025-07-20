package com.gestion.cuentas.streaming.dto;

import lombok.Data;

@Data
public class ServicioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String urlImg;
    private Integer cuentasTotal; // La meta manual que ya teníamos
    private Integer cuentasRegistradas; // ¡El nuevo campo calculado!
}