package com.gestion.cuentas.streaming.dto;

import com.gestion.cuentas.streaming.enums.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String numero;
    private String correo;
    private String idDiscord;
    private String linkWhatsapp;
    private String localidad;
    private String estadoEmocional;
    private String responsable;
    private TipoCliente tipoCliente;
}