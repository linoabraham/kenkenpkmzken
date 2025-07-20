package com.gestion.cuentas.streaming.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "perfiles") // Este será el nombre de la nueva tabla en la base de datos
@Getter
@Setter
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombrePerfil;

    private LocalDate fechaInicio;

    private LocalDate fechaRenovacion;

    private BigDecimal precioVenta;

    /**
     * Relación obligatoria: Un perfil siempre debe pertenecer a una cuenta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    /**
     * Relación opcional: Un perfil puede estar libre (cliente es nulo) o asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id") // nullable = true por defecto
    private Cliente cliente;
}