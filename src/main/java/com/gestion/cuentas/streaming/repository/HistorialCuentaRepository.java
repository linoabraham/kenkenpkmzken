package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.HistorialCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialCuentaRepository extends JpaRepository<HistorialCuenta, Long> {
    List<HistorialCuenta> findByCuentaAnteriorId(Long cuentaId);
    List<HistorialCuenta> findByCuentaNuevaId(Long cuentaId);
}