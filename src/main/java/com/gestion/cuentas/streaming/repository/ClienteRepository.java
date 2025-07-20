package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Cliente;
import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.enums.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByTipoCliente(TipoCliente tipoCliente);
    Optional<Cliente> findByCorreo(String correo);
    @Query("SELECT COUNT(DISTINCT c.cliente) FROM Cuenta c WHERE c.status = :status")
    long countDistinctByCuentasStatus(StatusCuenta status);
}