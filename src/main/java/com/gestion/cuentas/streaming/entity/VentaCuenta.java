package com.gestion.cuentas.streaming.entity;

import com.gestion.cuentas.streaming.enums.TipoCliente;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas_cuentas")
public class VentaCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_asignador_id", nullable = false)
    private Usuario usuarioAsignador;

    // --- CAMPO NUEVO ---
    // Esta relación vincula la venta a un perfil específico si aplica.
    // Es 'nullable = true' porque las ventas de cuentas COMPLETAS no tienen perfil.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = true)
    private Perfil perfil;

    // Constructores
    public VentaCuenta() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
    public Usuario getUsuarioAsignador() { return usuarioAsignador; }
    public void setUsuarioAsignador(Usuario usuarioAsignador) { this.usuarioAsignador = usuarioAsignador; }

    // --- NUEVO GETTER Y SETTER ---
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}