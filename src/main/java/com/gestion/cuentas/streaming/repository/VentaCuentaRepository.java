package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.VentaCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaCuentaRepository extends JpaRepository<VentaCuenta, Long> {
    @Query("SELECT v FROM VentaCuenta v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    List<VentaCuenta> findVentasDelDia();

    @Query("SELECT v FROM VentaCuenta v WHERE MONTH(v.fechaVenta) = MONTH(CURRENT_DATE) AND YEAR(v.fechaVenta) = YEAR(CURRENT_DATE)")
    List<VentaCuenta> findVentasDelMes();
}