package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.GananciaDTO;
import com.gestion.cuentas.streaming.dto.VentaCuentaDTO;
import com.gestion.cuentas.streaming.entity.VentaCuenta;
import com.gestion.cuentas.streaming.repository.VentaCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VentaCuentaService {

    private final VentaCuentaRepository ventaCuentaRepository;

    @Autowired
    public VentaCuentaService(VentaCuentaRepository ventaCuentaRepository){
        this.ventaCuentaRepository = ventaCuentaRepository;
    }

    public List<VentaCuentaDTO> findVentasDelDia() {
        return ventaCuentaRepository.findVentasDelDia().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VentaCuentaDTO> findVentasDelMes() {
        return ventaCuentaRepository.findVentasDelMes().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GananciaDTO getGananciasDelDia() {
        List<VentaCuenta> ventas = ventaCuentaRepository.findVentasDelDia();
        BigDecimal total = ventas.stream()
                .map(VentaCuenta::getPrecioVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new GananciaDTO(LocalDate.now(), ventas.size(), total);
    }

    public GananciaDTO getGananciasDelMes() {
        List<VentaCuenta> ventas = ventaCuentaRepository.findVentasDelMes();
        BigDecimal total = ventas.stream()
                .map(VentaCuenta::getPrecioVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new GananciaDTO(LocalDate.now().withDayOfMonth(1), ventas.size(), total);
    }

    /**
     * Convierte la entidad VentaCuenta a su DTO, incluyendo ahora el ID del perfil si existe.
     */
    private VentaCuentaDTO convertToDTO(VentaCuenta venta) {
        VentaCuentaDTO dto = new VentaCuentaDTO();
        dto.setId(venta.getId());
        dto.setCuentaId(venta.getCuenta().getId());
        dto.setClienteId(venta.getCliente().getId());
        dto.setPrecioVenta(venta.getPrecioVenta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTipoCliente(venta.getTipoCliente());
        dto.setUsuarioAsignadorId(venta.getUsuarioAsignador().getId());

        // Si la venta tiene un perfil asociado, se añade su ID al DTO.
        if (venta.getPerfil() != null) {
            dto.setPerfilId(venta.getPerfil().getId());
        }

        return dto;
    }
}