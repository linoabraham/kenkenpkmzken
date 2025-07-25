package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.ClienteDTO;
import com.gestion.cuentas.streaming.dto.CuentaAsignadaDTO;
import com.gestion.cuentas.streaming.dto.PerfilDTO;
import com.gestion.cuentas.streaming.dto.SuscripcionesClienteDTO;
import com.gestion.cuentas.streaming.entity.Cliente;
import com.gestion.cuentas.streaming.entity.Cuenta;
import com.gestion.cuentas.streaming.entity.Perfil;
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

    // --- ✅ NUEVO MÉTODO PRINCIPAL ---
    @Transactional(readOnly = true)
    public SuscripcionesClienteDTO getSuscripcionesPorCliente(Long clienteId) {
        // 1. Buscamos al cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + clienteId));

        SuscripcionesClienteDTO response = new SuscripcionesClienteDTO();
        response.setClienteId(cliente.getId());
        response.setNombreCliente(cliente.getNombre() + " " + cliente.getApellido());
        response.setNumeroCliente(cliente.getNumero());

        // 2. Obtenemos y convertimos sus cuentas COMPLETAS
        List<CuentaAsignadaDTO> cuentasDTO = cliente.getCuentas().stream()
                .map(this::convertCuentaToAsignadaDTO)
                .collect(Collectors.toList());
        response.setCuentasCompletas(cuentasDTO);

        // 3. Obtenemos y convertimos sus perfiles INDIVIDUALES
        List<PerfilDTO> perfilesDTO = cliente.getPerfilesAsignados().stream()
                .map(this::convertPerfilToDTO)
                .collect(Collectors.toList());
        response.setPerfilesIndividuales(perfilesDTO);

        return response;
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

    private CuentaAsignadaDTO convertCuentaToAsignadaDTO(Cuenta cuenta) {
        CuentaAsignadaDTO dto = new CuentaAsignadaDTO();
        dto.setCuentaId(cuenta.getId());
        dto.setCorreo(cuenta.getCorreo());
        if (cuenta.getServicio() != null) {
            dto.setNombreServicio(cuenta.getServicio().getNombre());
            dto.setUrlImgServicio(cuenta.getServicio().getUrlImg());
        }
        dto.setFechaInicio(cuenta.getFechaInicio());
        dto.setFechaRenovacion(cuenta.getFechaRenovacion());
        dto.setStatus(cuenta.getStatus().name());
        return dto;
    }

    // Este método es una copia del que está en CuentaService para mantener los servicios separados
    private PerfilDTO convertPerfilToDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setNombrePerfil(perfil.getNombrePerfil());
        if (perfil.getCuenta() != null) {
            dto.setCorreoCuenta(perfil.getCuenta().getCorreo());
            dto.setContraseña(perfil.getCuenta().getContraseña());
            dto.setPin(perfil.getCuenta().getPin());
            if (perfil.getCuenta().getServicio() != null) {
                dto.setUrlImg(perfil.getCuenta().getServicio().getUrlImg());
            }
        }
        if (perfil.getCliente() != null) {
            dto.setClienteId(perfil.getCliente().getId());
            dto.setNombreCliente(perfil.getCliente().getNombre());
            dto.setNumero(perfil.getCliente().getNumero());
        }
        dto.setFechaInicio(perfil.getFechaInicio());
        dto.setFechaRenovacion(perfil.getFechaRenovacion());
        dto.setPrecioVenta(perfil.getPrecioVenta());
        return dto;
    }
}