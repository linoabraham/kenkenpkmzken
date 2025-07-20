package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.DashboardDTO;
import com.gestion.cuentas.streaming.entity.Cuenta;
import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.enums.TipoCuenta;
import com.gestion.cuentas.streaming.repository.CuentaRepository;
import com.gestion.cuentas.streaming.repository.VentaCuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final CuentaRepository cuentaRepository;
    private final VentaCuentaRepository ventaCuentaRepository;

    @Autowired
    public DashboardService(CuentaRepository cuentaRepository, VentaCuentaRepository ventaCuentaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.ventaCuentaRepository = ventaCuentaRepository;
    }

    /**
     * Obtiene los datos consolidados para el Dashboard.
     * @return Un DTO con todas las métricas calculadas.
     */
    public DashboardDTO getDashboardData() {
        // Obtenemos todas las cuentas con estado ACTIVO
        List<Cuenta> cuentasActivas = cuentaRepository.findByStatus(StatusCuenta.ACTIVO);

        // Se calculan las métricas con la nueva lógica de entidades
        long totalPerfilesActivos = calcularPerfilesActivos(cuentasActivas);
        long totalClientesActivos = calcularClientesActivos(cuentasActivas);
        Map<String, Long> perfilesPorServicio = calcularPerfilesPorServicio(cuentasActivas);
        Map<String, Long> serviciosMasVendidos = calcularServiciosMasVendidos();

        return new DashboardDTO(totalPerfilesActivos, totalClientesActivos, perfilesPorServicio, serviciosMasVendidos);
    }

    /**
     * MÉTODO REFACTORIZADO
     * Calcula el número total de "unidades" activas.
     * Una cuenta COMPLETA cuenta como 1.
     * Para cuentas INDIVIDUALES, cuenta cada perfil asignado a un cliente.
     */
    private long calcularPerfilesActivos(List<Cuenta> cuentasActivas) {
        return cuentasActivas.stream()
                .mapToLong(cuenta -> {
                    if (cuenta.getTipoCuenta() == TipoCuenta.COMPLETO && cuenta.getCliente() != null) {
                        return 1L; // Cuenta completa cuenta como 1.
                    } else if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL) {
                        // Cuenta los perfiles que tienen un cliente asignado.
                        return cuenta.getPerfilesAsignados().stream()
                                .filter(perfil -> perfil.getCliente() != null)
                                .count();
                    }
                    return 0L;
                }).sum();
    }

    /**
     * MÉTODO REFACTORIZADO
     * Calcula el número de clientes únicos con al menos una cuenta completa o un perfil activo.
     */
    private long calcularClientesActivos(List<Cuenta> cuentasActivas) {
        // Se usa un Set para asegurar que cada cliente se cuente solo una vez.
        Set<Long> idsClientesActivos = cuentasActivas.stream()
                .flatMap(cuenta -> {
                    // Si la cuenta es COMPLETA, se obtiene el ID del cliente principal.
                    if (cuenta.getTipoCuenta() == TipoCuenta.COMPLETO && cuenta.getCliente() != null) {
                        return Stream.of(cuenta.getCliente().getId());
                    }
                    // Si la cuenta es INDIVIDUAL, se obtienen los IDs de los clientes de cada perfil asignado.
                    else if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL) {
                        return cuenta.getPerfilesAsignados().stream()
                                .filter(perfil -> perfil.getCliente() != null)
                                .map(perfil -> perfil.getCliente().getId());
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toSet());

        return idsClientesActivos.size();
    }

    /**
     * MÉTODO REFACTORIZADO
     * Agrupa por nombre de servicio y suma las "unidades" activas para cada uno.
     */
    private Map<String, Long> calcularPerfilesPorServicio(List<Cuenta> cuentasActivas) {
        return cuentasActivas.stream()
                .collect(Collectors.groupingBy(
                        cuenta -> cuenta.getServicio().getNombre(),
                        Collectors.summingLong(cuenta -> {
                            if (cuenta.getTipoCuenta() == TipoCuenta.COMPLETO && cuenta.getCliente() != null) {
                                return 1L;
                            } else if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL) {
                                return cuenta.getPerfilesAsignados().stream()
                                        .filter(perfil -> perfil.getCliente() != null)
                                        .count();
                            }
                            return 0L;
                        })
                ));
    }

    /**
     * MÉTODO SIN CAMBIOS
     * Este método ya funcionaba correctamente, ya que se basa en el repositorio de Ventas,
     * el cual se alimenta correctamente desde la nueva lógica de asignación.
     */
    private Map<String, Long> calcularServiciosMasVendidos() {
        return ventaCuentaRepository.findVentasDelMes().stream()
                .collect(Collectors.groupingBy(
                        venta -> venta.getCuenta().getServicio().getNombre(),
                        Collectors.counting()
                ));
    }

    // --- El método parsePerfilesJson() y la dependencia de ObjectMapper se han eliminado por completo. ---
}