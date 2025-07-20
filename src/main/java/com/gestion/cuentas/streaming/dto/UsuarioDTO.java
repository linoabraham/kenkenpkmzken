package com.gestion.cuentas.streaming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestion.cuentas.streaming.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private RolUsuario rolUsuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}