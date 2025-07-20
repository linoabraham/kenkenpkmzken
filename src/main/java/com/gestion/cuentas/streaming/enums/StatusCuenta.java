package com.gestion.cuentas.streaming.enums;

/**
 * Define el estado actual de una cuenta de streaming.
 * SINUSAR:CUANDO LA CUENTA NO ESTA ASIGNADO NADA SOLO CORREO Y idSERVICIO
 * ACTIVO: La cuenta está en uso por un cliente y su pago está al día.
 * VENCIDO: La cuenta ha expirado y no ha sido renovada. Está disponible.
 * REEMPLAZADA: La cuenta fue cambiada por otra debido a un problema y ya no está en uso.
 */
public enum StatusCuenta {
    ACTIVO,
    VENCIDO,
    REEMPLAZADA,
    SINUSAR,
    REPORTADO
}