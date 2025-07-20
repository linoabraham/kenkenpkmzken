package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    /**
     * Busca todos los perfiles de una cuenta que no tienen un cliente asignado.
     *
     * @param cuentaId El ID de la cuenta.
     * @return Una lista de perfiles disponibles.
     */
    // ✅ CORRECCIÓN: El método ahora se llama correctamente y solo necesita un parámetro.
    List<Perfil> findByCuentaIdAndClienteIsNull(Long cuentaId);
    List<Perfil> findByClienteIsNotNullAndFechaRenovacionBefore(LocalDate fecha);
}