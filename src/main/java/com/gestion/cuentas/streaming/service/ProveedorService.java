package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.ProveedorDTO;
import com.gestion.cuentas.streaming.entity.Proveedor;
import com.gestion.cuentas.streaming.repository.ProveedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public ProveedorDTO create(ProveedorDTO proveedorDTO) {
        Proveedor proveedor = new Proveedor();
        mapDtoToEntity(proveedorDTO, proveedor);
        Proveedor saved = proveedorRepository.save(proveedor);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProveedorDTO> findAll() {
        return proveedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProveedorDTO findById(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + id));
        return convertToDTO(proveedor);
    }

    public ProveedorDTO update(Long id, ProveedorDTO proveedorDTO) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + id));
        mapDtoToEntity(proveedorDTO, proveedor);
        Proveedor updated = proveedorRepository.save(proveedor);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Proveedor no encontrado con id: " + id);
        }
        proveedorRepository.deleteById(id);
    }

    private ProveedorDTO convertToDTO(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setCorreo(proveedor.getCorreo());
        dto.setNumero(proveedor.getNumero());
        dto.setLinkWhatsapp(proveedor.getLinkWhatsapp());
        dto.setTipoServicio(proveedor.getTipoServicio());
        dto.setTipoCuentaQueProvee(proveedor.getTipoCuentaQueProvee());
        dto.setPrecioReferencial(proveedor.getPrecioReferencial());
        return dto;
    }

    private void mapDtoToEntity(ProveedorDTO dto, Proveedor entity) {
        entity.setNombre(dto.getNombre());
        entity.setCorreo(dto.getCorreo());
        entity.setNumero(dto.getNumero());
        entity.setLinkWhatsapp(dto.getLinkWhatsapp());
        entity.setTipoServicio(dto.getTipoServicio());
        entity.setTipoCuentaQueProvee(dto.getTipoCuentaQueProvee());
        entity.setPrecioReferencial(dto.getPrecioReferencial());
    }
}