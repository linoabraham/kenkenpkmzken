package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.GananciaDTO;
import com.gestion.cuentas.streaming.dto.TopClienteDTO;
import com.gestion.cuentas.streaming.dto.VentaCuentaDTO;
import com.gestion.cuentas.streaming.entity.VentaCuenta;
import com.gestion.cuentas.streaming.repository.VentaCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    // Añade estos métodos a tu VentaCuentaService.java

    public List<TopClienteDTO> findTopClientesDelDia() {
        List<VentaCuenta> ventas = ventaCuentaRepository.findVentasDelDia();
        return calcularTopClientes(ventas);
    }

    public List<TopClienteDTO> findTopClientesDeLaSemana() {
        // En el método findTopClientesDeLaSemana()
        LocalDateTime inicioDeSemana = LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        List<VentaCuenta> ventas = ventaCuentaRepository.findVentasDeLaSemana(inicioDeSemana);
        return calcularTopClientes(ventas);
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

        if (venta.getCuenta().getServicio() != null) {
            dto.setNombreServicio(venta.getCuenta().getServicio().getNombre());
            // ✅ LÓGICA AÑADIDA para la imagen
            dto.setUrlImg(venta.getCuenta().getServicio().getUrlImg());
        }

        if (venta.getPerfil() != null) {
            dto.setPerfilId(venta.getPerfil().getId());
        }

        return dto;
    }

    // Método auxiliar privado para procesar la lista de ventas
    private List<TopClienteDTO> calcularTopClientes(List<VentaCuenta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(
                        VentaCuenta::getCliente, // Agrupa por objeto Cliente
                        Collectors.counting()    // Cuenta cuántas ventas tiene cada uno
                ))
                .entrySet().stream() // Convierte el mapa a un stream de entradas (Cliente, Conteo)
                .map(entry -> new TopClienteDTO(
                        entry.getKey().getId(),
                        entry.getKey().getNombre() + " " + entry.getKey().getApellido(),
                        entry.getKey().getNumero(),
                        entry.getValue()
                ))
                .sorted((c1, c2) -> c2.getTotalCompras().compareTo(c1.getTotalCompras())) // Ordena de mayor a menor
                .collect(Collectors.toList());
    }

}