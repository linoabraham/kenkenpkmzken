package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByNombreContaining(String nombre);
}