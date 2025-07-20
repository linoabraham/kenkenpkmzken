package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.ReporteCuentaDTO;
import com.gestion.cuentas.streaming.entity.Cuenta;
import com.gestion.cuentas.streaming.entity.ReporteCuenta;
import com.gestion.cuentas.streaming.entity.Usuario;
import com.gestion.cuentas.streaming.repository.CuentaRepository;
import com.gestion.cuentas.streaming.repository.ReporteCuentaRepository;
import com.gestion.cuentas.streaming.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReporteCuentaService {

    @Autowired
    private ReporteCuentaRepository reporteCuentaRepository;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public ReporteCuentaDTO create(ReporteCuentaDTO reporteDTO) {
        Cuenta cuenta = cuentaRepository.findById(reporteDTO.getCuentaId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada para el reporte, id: " + reporteDTO.getCuentaId()));

        Usuario usuario = usuarioRepository.findById(reporteDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para el reporte, id: " + reporteDTO.getUsuarioId()));

        ReporteCuenta reporte = new ReporteCuenta();
        reporte.setCuenta(cuenta);
        reporte.setUsuario(usuario);
        reporte.setFecha(LocalDate.now());
        reporte.setMotivo(reporteDTO.getMotivo());
        reporte.setDetalle(reporteDTO.getDetalle());

        ReporteCuenta saved = reporteCuentaRepository.save(reporte);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ReporteCuentaDTO> findAll() {
        return reporteCuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReporteCuentaDTO findById(Long id) {
        ReporteCuenta reporte = reporteCuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado con id: " + id));
        return convertToDTO(reporte);
    }

    private ReporteCuentaDTO convertToDTO(ReporteCuenta reporte) {
        ReporteCuentaDTO dto = new ReporteCuentaDTO();
        dto.setId(reporte.getId());
        dto.setCuentaId(reporte.getCuenta().getId());
        dto.setUsuarioId(reporte.getUsuario().getId());
        dto.setFecha(reporte.getFecha());
        dto.setMotivo(reporte.getMotivo());
        dto.setDetalle(reporte.getDetalle());
        return dto;
    }
}