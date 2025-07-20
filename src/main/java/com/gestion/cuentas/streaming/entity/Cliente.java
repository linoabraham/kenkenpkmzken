package com.gestion.cuentas.streaming.entity;

import com.gestion.cuentas.streaming.enums.TipoCliente;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    // Esta relación se mantiene para las cuentas de tipo COMPLETO
    @OneToMany(mappedBy = "cliente")
    private List<Cuenta> cuentas = new ArrayList<>();

    // --- RELACIÓN AÑADIDA ---
    // Se añade la relación inversa para acceder a los perfiles asignados a este cliente.
    @OneToMany(mappedBy = "cliente")
    private List<Perfil> perfilesAsignados = new ArrayList<>();


    // Getters y Setters (solo se muestran los nuevos, los demás no cambian)
    public List<Perfil> getPerfilesAsignados() {
        return perfilesAsignados;
    }

    public void setPerfilesAsignados(List<Perfil> perfilesAsignados) {
        this.perfilesAsignados = perfilesAsignados;
    }

    // --- El resto de Getters y Setters permanecen igual ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getIdDiscord() { return idDiscord; }
    public void setIdDiscord(String idDiscord) { this.idDiscord = idDiscord; }
    public String getLinkWhatsapp() { return linkWhatsapp; }
    public void setLinkWhatsapp(String linkWhatsapp) { this.linkWhatsapp = linkWhatsapp; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getEstadoEmocional() { return estadoEmocional; }
    public void setEstadoEmocional(String estadoEmocional) { this.estadoEmocional = estadoEmocional; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
    public List<Cuenta> getCuentas() { return cuentas; }
    public void setCuentas(List<Cuenta> cuentas) { this.cuentas = cuentas; }
}