package com.gestion.cuentas.streaming.repository;

import com.gestion.cuentas.streaming.entity.Usuario;
import com.gestion.cuentas.streaming.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByRolUsuario(RolUsuario rolUsuario);
}