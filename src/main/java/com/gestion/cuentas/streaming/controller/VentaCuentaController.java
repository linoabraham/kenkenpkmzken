package com.gestion.cuentas.streaming.controller;

import com.gestion.cuentas.streaming.dto.GananciaDTO;
import com.gestion.cuentas.streaming.dto.VentaCuentaDTO;
import com.gestion.cuentas.streaming.service.VentaCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaCuentaController {

    @Autowired
    private VentaCuentaService ventaCuentaService;

    @GetMapping("/diarias/resumen")
    public ResponseEntity<GananciaDTO> getGananciasDiarias() {
        return ResponseEntity.ok(ventaCuentaService.getGananciasDelDia());
    }

    @GetMapping("/mensuales/resumen")
    public ResponseEntity<GananciaDTO> getGananciasMensuales() {
        return ResponseEntity.ok(ventaCuentaService.getGananciasDelMes());
    }

    @GetMapping("/diarias/lista")
    public ResponseEntity<List<VentaCuentaDTO>> getVentasDiarias() {
        return ResponseEntity.ok(ventaCuentaService.findVentasDelDia());
    }



    @GetMapping("/mensuales/lista")
    public ResponseEntity<List<VentaCuentaDTO>> getVentasMensuales() {
        return ResponseEntity.ok(ventaCuentaService.findVentasDelMes());
    }
}