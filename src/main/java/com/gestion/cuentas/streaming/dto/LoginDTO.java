package com.gestion.cuentas.streaming.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String correo;
    private String password;
}