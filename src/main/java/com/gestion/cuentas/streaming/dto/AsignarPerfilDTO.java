package com.gestion.cuentas.streaming.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class AsignarPerfilDTO {
    private Long cuentaId;
    private Long clienteId;
    private List<String> perfilesNuevos; // Lista de nombres de los perfiles a asignar
    private Long usuarioAsignadorId;
    private BigDecimal precioVenta;// Precio por esta asignación de perfiles
    // Nuevos campos
    private LocalDate fechaInicioPerfiles;
    private LocalDate fechaRenovacionPerfiles;
}