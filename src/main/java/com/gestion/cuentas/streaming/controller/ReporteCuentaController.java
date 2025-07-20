package com.gestion.cuentas.streaming.controller;

import com.gestion.cuentas.streaming.dto.ReporteAsignacionDTO;
import com.gestion.cuentas.streaming.dto.ReporteCuentaDTO;
import com.gestion.cuentas.streaming.service.CuentaService;
import com.gestion.cuentas.streaming.service.ReporteCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteCuentaController {

    @Autowired
    private ReporteCuentaService reporteCuentaService;

    // --- NUEVA DEPENDENCIA ---
    // Se inyecta CuentaService para poder generar el nuevo reporte de asignaciones.
    @Autowired
    private CuentaService cuentaService;

    // --- NUEVO ENDPOINT DE REPORTE DE NEGOCIO ---
    /**
     * GET para obtener el reporte de estado y asignación de todas las cuentas.
     * Muestra a qué cliente está asignada una cuenta COMPLETA o qué perfiles
     * de una cuenta INDIVIDUAL están asignados a qué clientes.
     *
     * @return Una lista con el estado de asignación de cada cuenta.
     */
    @GetMapping("/asignaciones")
    public ResponseEntity<List<ReporteAsignacionDTO>> getReporteDeAsignaciones() {
        return ResponseEntity.ok(cuentaService.getReporteDeAsignaciones());
    }

    // --- SOLO SE MANTIENEN LOS ENDPOINTS GET PARA CONSULTAR ---
    @GetMapping
    public ResponseEntity<List<ReporteCuentaDTO>> findAll() {
        return ResponseEntity.ok(reporteCuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteCuentaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteCuentaService.findById(id));
    }
}