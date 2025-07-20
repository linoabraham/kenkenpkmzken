package com.gestion.cuentas.streaming.controller;

import com.gestion.cuentas.streaming.dto.ServicioDTO;
import com.gestion.cuentas.streaming.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioDTO> create(@RequestBody ServicioDTO servicioDTO) {
        return new ResponseEntity<>(servicioService.create(servicioDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> findAll() {
        return ResponseEntity.ok(servicioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDTO> update(@PathVariable Long id, @RequestBody ServicioDTO servicioDTO) {
        return ResponseEntity.ok(servicioService.update(id, servicioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}