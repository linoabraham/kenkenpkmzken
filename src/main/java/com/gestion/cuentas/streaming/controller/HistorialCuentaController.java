package com.gestion.cuentas.streaming.controller;

import com.gestion.cuentas.streaming.dto.HistorialCuentaDTO;
import com.gestion.cuentas.streaming.service.HistorialCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/historial")
public class HistorialCuentaController {

    @Autowired
    private HistorialCuentaService historialCuentaService;

    @GetMapping
    public ResponseEntity<List<HistorialCuentaDTO>> findAll() {
        return ResponseEntity.ok(historialCuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialCuentaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historialCuentaService.findById(id));
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<HistorialCuentaDTO>> findByCuentaAnteriorId(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(historialCuentaService.findByCuentaAnteriorId(cuentaId));
    }
}