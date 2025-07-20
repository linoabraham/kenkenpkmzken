package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.UsuarioDTO;
import com.gestion.cuentas.streaming.entity.Usuario;
import com.gestion.cuentas.streaming.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByCorreo(usuarioDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setPassword(usuarioDTO.getPassword()); // Sin hash por simplicidad
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setRolUsuario(usuarioDTO.getRolUsuario());
        Usuario saved = usuarioRepository.save(usuario);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        return convertToDTO(usuario);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        if (!usuario.getCorreo().equals(usuarioDTO.getCorreo()) && usuarioRepository.findByCorreo(usuarioDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso");
        }

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setRolUsuario(usuarioDTO.getRolUsuario());
        // No se actualiza la contraseña desde este método por seguridad.

        Usuario updated = usuarioRepository.save(usuario);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        return convertToDTO(usuario);
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setRolUsuario(usuario.getRolUsuario());
        return dto;
    }
}