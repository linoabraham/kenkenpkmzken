package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.ClienteDTO;
import com.gestion.cuentas.streaming.entity.Cliente;
import com.gestion.cuentas.streaming.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO create(ClienteDTO clienteDTO) {
        if (clienteDTO.getCorreo() != null && !clienteDTO.getCorreo().isEmpty() && clienteRepository.findByCorreo(clienteDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso por otro cliente");
        }

        Cliente cliente = new Cliente();
        mapDtoToEntity(clienteDTO, cliente);
        Cliente saved = clienteRepository.save(cliente);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDTO(cliente);
    }

    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));

        if (clienteDTO.getCorreo() != null && !clienteDTO.getCorreo().isEmpty() && !cliente.getCorreo().equals(clienteDTO.getCorreo()) && clienteRepository.findByCorreo(clienteDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está en uso por otro cliente");
        }

        mapDtoToEntity(clienteDTO, cliente);
        Cliente updated = clienteRepository.save(cliente);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setNumero(cliente.getNumero());
        dto.setCorreo(cliente.getCorreo());
        dto.setIdDiscord(cliente.getIdDiscord());
        dto.setLinkWhatsapp(cliente.getLinkWhatsapp());
        dto.setLocalidad(cliente.getLocalidad());
        dto.setEstadoEmocional(cliente.getEstadoEmocional());
        dto.setResponsable(cliente.getResponsable());
        dto.setTipoCliente(cliente.getTipoCliente());
        return dto;
    }

    private void mapDtoToEntity(ClienteDTO dto, Cliente entity) {
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setNumero(dto.getNumero());
        entity.setCorreo(dto.getCorreo());
        entity.setIdDiscord(dto.getIdDiscord());
        entity.setLinkWhatsapp(dto.getLinkWhatsapp());
        entity.setLocalidad(dto.getLocalidad());
        entity.setEstadoEmocional(dto.getEstadoEmocional());
        entity.setResponsable(dto.getResponsable());
        entity.setTipoCliente(dto.getTipoCliente());
    }
}