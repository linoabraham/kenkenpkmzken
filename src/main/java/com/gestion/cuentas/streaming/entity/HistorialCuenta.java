package com.gestion.cuentas.streaming.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cuenta")
public class HistorialCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_anterior_id")
    private Cuenta cuentaAnterior;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_nueva_id")
    private Cuenta cuentaNueva;

    private String motivo;
    private LocalDateTime fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Constructores
    public HistorialCuenta() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuenta getCuentaAnterior() {
        return cuentaAnterior;
    }

    public void setCuentaAnterior(Cuenta cuentaAnterior) {
        this.cuentaAnterior = cuentaAnterior;
    }

    public Cuenta getCuentaNueva() {
        return cuentaNueva;
    }

    public void setCuentaNueva(Cuenta cuentaNueva) {
        this.cuentaNueva = cuentaNueva;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}