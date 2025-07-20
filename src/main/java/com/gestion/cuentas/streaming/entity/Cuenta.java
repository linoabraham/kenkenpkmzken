package com.gestion.cuentas.streaming.entity;

import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.enums.TipoCuenta;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correo;
    private String contraseña;
    private String pin;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Perfil> perfilesAsignados = new ArrayList<>();

    private Integer perfilesMaximos;
    private String enlace;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_renovacion")
    private LocalDate fechaRenovacion;

    @Enumerated(EnumType.STRING)
    private StatusCuenta status;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    // --- GETTER Y SETTER PARA LA NUEVA LISTA ---
    public List<Perfil> getPerfilesAsignados() { return perfilesAsignados; }
    public void setPerfilesAsignados(List<Perfil> perfilesAsignados) { this.perfilesAsignados = perfilesAsignados; }

    public Integer getPerfilesMaximos() { return perfilesMaximos; }
    public void setPerfilesMaximos(Integer perfilesMaximos) { this.perfilesMaximos = perfilesMaximos; }
    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaRenovacion() { return fechaRenovacion; }
    public void setFechaRenovacion(LocalDate fechaRenovacion) { this.fechaRenovacion = fechaRenovacion; }
    public StatusCuenta getStatus() { return status; }
    public void setStatus(StatusCuenta status) { this.status = status; }
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}