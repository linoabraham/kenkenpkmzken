package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByTipoServicio(String tipoServicio);
}