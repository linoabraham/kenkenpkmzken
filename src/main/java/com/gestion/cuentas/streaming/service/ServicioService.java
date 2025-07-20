package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.ServicioDTO;
import com.gestion.cuentas.streaming.entity.Servicio;
import com.gestion.cuentas.streaming.repository.ServicioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public ServicioDTO create(ServicioDTO servicioDTO) {
        Servicio servicio = new Servicio();
        mapDtoToEntity(servicioDTO, servicio);
        Servicio saved = servicioRepository.save(servicio);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ServicioDTO> findAll() {
        return servicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicioDTO findById(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + id));
        return convertToDTO(servicio);
    }

    public ServicioDTO update(Long id, ServicioDTO servicioDTO) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + id));
        mapDtoToEntity(servicioDTO, servicio);
        Servicio updated = servicioRepository.save(servicio);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new EntityNotFoundException("Servicio no encontrado con id: " + id);
        }
        servicioRepository.deleteById(id);
    }

    private ServicioDTO convertToDTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setUrlImg(servicio.getUrlImg());
        dto.setCuentasTotal(servicio.getCuentasTotal());
        // --- AQUÍ ESTÁ LA MAGIA ---
        // Contamos el número de elementos en la lista de cuentas del servicio
        if (servicio.getCuentas() != null) {
            dto.setCuentasRegistradas(servicio.getCuentas().size());
        } else {
            dto.setCuentasRegistradas(0);
        }
        return dto;

    }

    private void mapDtoToEntity(ServicioDTO dto, Servicio entity) {
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setUrlImg(dto.getUrlImg());
        entity.setCuentasTotal(dto.getCuentasTotal());
    }
}