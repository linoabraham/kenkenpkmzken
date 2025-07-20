package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.ReporteCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteCuentaRepository extends JpaRepository<ReporteCuenta, Long> {
    List<ReporteCuenta> findByCuentaId(Long cuentaId);
    List<ReporteCuenta> findByUsuarioId(Long usuarioId);
}