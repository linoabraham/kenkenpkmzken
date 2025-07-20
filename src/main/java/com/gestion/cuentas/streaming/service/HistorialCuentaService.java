package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.HistorialCuentaDTO;
import com.gestion.cuentas.streaming.entity.HistorialCuenta;
import com.gestion.cuentas.streaming.repository.HistorialCuentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class HistorialCuentaService {

    @Autowired
    private HistorialCuentaRepository historialCuentaRepository;

    public List<HistorialCuentaDTO> findAll() {
        return historialCuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public HistorialCuentaDTO findById(Long id) {
        HistorialCuenta historial = historialCuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de historial no encontrado con id: " + id));
        return convertToDTO(historial);
    }

    public List<HistorialCuentaDTO> findByCuentaAnteriorId(Long cuentaId) {
        return historialCuentaRepository.findByCuentaAnteriorId(cuentaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HistorialCuentaDTO convertToDTO(HistorialCuenta historial) {
        HistorialCuentaDTO dto = new HistorialCuentaDTO();
        dto.setId(historial.getId());
        dto.setCuentaAnteriorId(historial.getCuentaAnterior().getId());
        dto.setCuentaNuevaId(historial.getCuentaNueva().getId());
        dto.setMotivo(historial.getMotivo());
        dto.setFechaCambio(historial.getFechaCambio());
        dto.setUsuarioId(historial.getUsuario().getId());
        return dto;
    }
}