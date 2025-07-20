package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Cuenta;
import com.gestion.cuentas.streaming.enums.StatusCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByStatus(StatusCuenta status);
    List<Cuenta> findByServicioId(Long servicioId);
    List<Cuenta> findByClienteId(Long clienteId);
    List<Cuenta> findByStatusAndClienteIsNull(StatusCuenta status);
    Optional<Cuenta> findByCorreo(String correo);

    @Query("SELECT c FROM Cuenta c WHERE c.fechaRenovacion < CURRENT_DATE AND c.status = 'ACTIVO'")
    List<Cuenta> findCuentasVencidas();
    long countByStatus(StatusCuenta status);
}