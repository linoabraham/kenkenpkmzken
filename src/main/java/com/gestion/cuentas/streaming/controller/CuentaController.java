package com.gestion.cuentas.streaming.controller;

import com.gestion.cuentas.streaming.dto.*;
import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    // --- NUEVO ENDPOINT ---
    /**
     * PATCH para liberar un perfil específico, desvinculándolo de su cliente.
     * Se usa PATCH porque es una actualización parcial del estado del perfil.
     *
     * @param perfilId El ID del perfil que se va a liberar.
     * @return Una respuesta sin contenido (204 No Content) si la operación es exitosa.
     */
    @PatchMapping("/perfiles/{perfilId}/liberar")
    public ResponseEntity<Void> liberarPerfil(@PathVariable Long perfilId) {
        cuentaService.liberarPerfil(perfilId);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // --- ENDPOINTS EXISTENTES (Sin cambios en su firma) ---
    // La lógica interna de estos endpoints ahora es más robusta gracias
    // a la refactorización del servicio, pero su declaración no cambia.
    // =================================================================

    @PostMapping
    public ResponseEntity<CuentaDTO> create(@RequestBody CuentaDTO cuentaDTO) {
        return new ResponseEntity<>(cuentaService.create(cuentaDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> findAll() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CuentaDTO>> searchCuentas(
            @RequestParam(required = false) StatusCuenta status,
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(cuentaService.buscarCuentas(status, servicioId, clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> update(@PathVariable Long id, @RequestBody CuentaDTO cuentaDTO) {
        return ResponseEntity.ok(cuentaService.update(id, cuentaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/asignar")
    public ResponseEntity<VentaCuentaDTO> asignarCuenta(@RequestBody AsignarCuentaDTO asignarCuentaDTO) {
        return ResponseEntity.ok(cuentaService.asignarCuenta(asignarCuentaDTO));
    }

    @PostMapping("/asignar-lote")
    public ResponseEntity<List<VentaCuentaDTO>> asignarLote(@RequestBody AsignarLoteDTO asignarLoteDTO) {
        return ResponseEntity.ok(cuentaService.asignarLote(asignarLoteDTO));
    }
    @GetMapping("/{cuentaId}/perfiles")
    public ResponseEntity<List<PerfilDTO>> getPerfilesDeCuenta(@PathVariable Long cuentaId) {
        List<PerfilDTO> perfiles = cuentaService.getPerfilesDeCuenta(cuentaId);
        return ResponseEntity.ok(perfiles);
    }

// ANTES:
// public ResponseEntity<CuentaDTO> asignarPerfiles(@RequestBody AsignarPerfilDTO asignarPerfilDTO) { ... }

    // AHORA:
    @PostMapping("/asignar-perfiles")
    public ResponseEntity<AsignacionPerfilResponseDTO> asignarPerfiles(@RequestBody AsignarPerfilDTO asignarPerfilDTO) {
        AsignacionPerfilResponseDTO respuesta = cuentaService.asignarPerfiles(asignarPerfilDTO);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/vencer-automaticonosirvep")
    public ResponseEntity<List<CuentaDTO>> vencerAutomatico() {
        return ResponseEntity.ok(cuentaService.vencerAutomatico());
    }





    @PostMapping("/{id}/reportar")
    public ResponseEntity<CuentaDTO> reportarCuenta(@PathVariable Long id, @RequestBody ReportarCuentaDTO dto) {
        CuentaDTO cuentaReportada = cuentaService.reportarCuenta(id, dto);
        return ResponseEntity.ok(cuentaReportada);
    }

    @PostMapping("/{id}/reemplazar-completa")
    public ResponseEntity<HistorialCuentaDTO> reemplazarCuentaCompleta(@PathVariable Long id, @RequestBody ReemplazarCuentaDTO dto) {
        HistorialCuentaDTO historial = cuentaService.reemplazarCuentaCompleta(id, dto);
        return ResponseEntity.ok(historial);
    }

    @PostMapping("/{id}/reemplazar-individual")
    public ResponseEntity<HistorialCuentaDTO> reemplazarCuentaIndividual(@PathVariable Long id, @RequestBody ReemplazarCuentaDTO dto) {
        HistorialCuentaDTO historial = cuentaService.reemplazarCuentaIndividual(id, dto);
        return ResponseEntity.ok(historial);
    }


    @PatchMapping("/{id}/renovar-suscripcioncomple")
    public ResponseEntity<CuentaDTO> renovarSuscripcionCompleta(@PathVariable Long id, @RequestBody RenovarSuscripcionDTO dto) {
        CuentaDTO cuentaRenovada = cuentaService.renovarSuscripcionCompleta(id, dto);
        return ResponseEntity.ok(cuentaRenovada);
    }


    @PatchMapping("/perfiles/{id}/renovar-suscripcion")
    public ResponseEntity<PerfilDTO> renovarSuscripcionPerfil(@PathVariable Long id, @RequestBody RenovarSuscripcionDTO dto) {
        PerfilDTO perfilRenovado = cuentaService.renovarSuscripcionPerfil(id, dto);
        return ResponseEntity.ok(perfilRenovado);
    }

    @GetMapping("/perfiles/vencidos")
    public ResponseEntity<List<PerfilDTO>> findPerfilesVencidos() {
        List<PerfilDTO> perfilesVencidos = cuentaService.findPerfilesVencidos();
        return ResponseEntity.ok(perfilesVencidos);
    }

    // Añade este método a tu CuentaController.java

    @PatchMapping("/{id}/cambiar-contrasenaenuso")
    public ResponseEntity<CuentaDTO> cambiarContrasena(
            @PathVariable Long id,
            @RequestBody CambiarContrasenaDTO dto) {

        CuentaDTO cuentaActualizada = cuentaService.cambiarContrasena(id, dto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @PatchMapping("/{id}/absolver-cuentareportada")
    public ResponseEntity<CuentaDTO> absolverCuentaReportada(@PathVariable Long id) {
        CuentaDTO cuentaActualizada = cuentaService.absolverCuentaReportada(id);
        return ResponseEntity.ok(cuentaActualizada);
    }




}