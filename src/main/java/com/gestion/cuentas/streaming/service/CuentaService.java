package com.gestion.cuentas.streaming.service;

import com.gestion.cuentas.streaming.dto.*;
import com.gestion.cuentas.streaming.entity.*;
import com.gestion.cuentas.streaming.enums.StatusCuenta;
import com.gestion.cuentas.streaming.enums.TipoCliente;
import com.gestion.cuentas.streaming.enums.TipoCuenta;
import com.gestion.cuentas.streaming.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaCuentaRepository ventaCuentaRepository;
    private final HistorialCuentaRepository historialCuentaRepository;
    private final PerfilRepository perfilRepository;
    private final ReporteCuentaRepository reporteCuentaRepository;

    @Autowired
    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository,
                         ServicioRepository servicioRepository, UsuarioRepository usuarioRepository,
                         VentaCuentaRepository ventaCuentaRepository, HistorialCuentaRepository historialCuentaRepository,
                         PerfilRepository perfilRepository,
                         ReporteCuentaRepository reporteCuentaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaCuentaRepository = ventaCuentaRepository;
        this.historialCuentaRepository = historialCuentaRepository;
        this.perfilRepository = perfilRepository;
        this.reporteCuentaRepository = reporteCuentaRepository;
    }

    // ... (Todos los métodos anteriores sin cambios) ...

    public CuentaDTO create(CuentaDTO cuentaDTO) {
        if (cuentaDTO.getCorreo() != null && !cuentaDTO.getCorreo().isEmpty() && cuentaRepository.findByCorreo(cuentaDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con este correo");
        }
        Servicio servicio = servicioRepository.findById(cuentaDTO.getServicioId())
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + cuentaDTO.getServicioId()));

        Cuenta cuenta = new Cuenta();
        mapDtoToEntity(cuentaDTO, cuenta);
        cuenta.setServicio(servicio);
        cuenta.setStatus(StatusCuenta.SINUSAR);

        if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL && cuenta.getPerfilesMaximos() != null && cuenta.getPerfilesMaximos() > 0) {
            List<Perfil> perfilesNuevos = new ArrayList<>();
            for (int i = 1; i <= cuenta.getPerfilesMaximos(); i++) {
                Perfil perfil = new Perfil();
                perfil.setNombrePerfil("Perfil " + i);
                perfil.setCuenta(cuenta);
                perfilesNuevos.add(perfil);
            }
            cuenta.setPerfilesAsignados(perfilesNuevos);
        }

        Cuenta saved = cuentaRepository.save(cuenta);
        return convertToDTO(saved);
    }
    /**
     * Obtiene todos los perfiles (asignados y libres) de una cuenta específica.
     *
     * @param cuentaId El ID de la cuenta a consultar.
     * @return Una lista de PerfilDTO con los detalles de cada perfil.
     */
    @Transactional(readOnly = true)
    public List<PerfilDTO> getPerfilesDeCuenta(Long cuentaId) {
        // 1. Buscamos la cuenta para asegurarnos de que existe
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + cuentaId));

        // 2. Convertimos la lista de perfiles de la entidad a una lista de DTOs
        //    usando el método auxiliar que ya tenemos.
        return cuenta.getPerfilesAsignados().stream()
                .map(this::convertPerfilToDTO)
                .collect(Collectors.toList());
    }






// Reemplaza el método completo en tu archivo: com.gestion.cuentas.streaming.service.CuentaService.java

    public AsignacionPerfilResponseDTO asignarPerfiles(AsignarPerfilDTO dto) {
        // 1. OBTENER ENTIDADES PRINCIPALES (sin cambios)
        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + dto.getCuentaId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + dto.getClienteId()));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioAsignadorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario asignador no encontrado con id: " + dto.getUsuarioAsignadorId()));

        // 2. VALIDACIONES INICIALES (sin cambios)
        if (cuenta.getTipoCuenta() != TipoCuenta.INDIVIDUAL) {
            throw new IllegalArgumentException("Solo se pueden asignar perfiles a cuentas de tipo INDIVIDUAL.");
        }
        if (dto.getPerfilesNuevos() == null || dto.getPerfilesNuevos().isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un nombre de perfil para asignar.");
        }

        List<Perfil> perfilesDisponibles = perfilRepository.findByCuentaIdAndClienteIsNull(cuenta.getId());

        if (perfilesDisponibles.size() < dto.getPerfilesNuevos().size()) {
            throw new IllegalArgumentException("No hay suficientes perfiles disponibles en la cuenta. " +
                    "Disponibles: " + perfilesDisponibles.size() + ", Solicitados: " + dto.getPerfilesNuevos().size());
        }

        LocalDate fechaInicio = dto.getFechaInicioPerfiles() != null ? dto.getFechaInicioPerfiles() : LocalDate.now();
        LocalDate fechaRenovacion = dto.getFechaRenovacionPerfiles() != null ? dto.getFechaRenovacionPerfiles() : fechaInicio.plusDays(30);

        List<Perfil> perfilesModificados = new ArrayList<>();

        // 3. ITERAR Y ASIGNAR (sin cambios en la lógica, solo el nombre de la lista)
        for (int i = 0; i < dto.getPerfilesNuevos().size(); i++) {
            Perfil perfil = perfilesDisponibles.get(i);
            String nuevoNombrePerfil = dto.getPerfilesNuevos().get(i);

            perfil.setNombrePerfil(nuevoNombrePerfil);
            perfil.setCliente(cliente);
            perfil.setFechaInicio(fechaInicio);
            perfil.setFechaRenovacion(fechaRenovacion);
            perfil.setPrecioVenta(dto.getPrecioVenta());

            registrarVenta(cuenta, perfil, cliente, perfil.getPrecioVenta(), usuario);
            perfilesModificados.add(perfil);
        }

        if (cuenta.getStatus() == StatusCuenta.SINUSAR) {
            cuenta.setStatus(StatusCuenta.ACTIVO);
            cuenta.setFechaInicio(fechaInicio);
            cuenta.setFechaRenovacion(fechaRenovacion);
        }

        // 4. GUARDAR LOS CAMBIOS (sin cambios)
        perfilRepository.saveAll(perfilesModificados);
        cuentaRepository.save(cuenta);

        // 5. ✅ CONSTRUIR Y DEVOLVER LA NUEVA RESPUESTA DETALLADA
        List<PerfilDTO> perfilesAsignadosDTOs = perfilesModificados.stream()
                .map(this::convertPerfilToDTO)
                .collect(Collectors.toList());

        AsignacionPerfilResponseDTO response = new AsignacionPerfilResponseDTO();
        response.setCuentaId(cuenta.getId());
        response.setCorreoCuenta(cuenta.getCorreo());
        response.setStatusCuenta(cuenta.getStatus().toString());
        response.setPerfilesAsignadosExitosamente(perfilesAsignadosDTOs);

        return response;
    }







    public void liberarPerfil(Long perfilId) {
        // 1. Buscamos el perfil (esto no cambia)
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con id: " + perfilId));

        // 2. Limpiamos los datos de la asignación (esto no cambia)
        perfil.setCliente(null);
        perfil.setFechaInicio(null);
        perfil.setFechaRenovacion(null);
        perfil.setPrecioVenta(null);

        // 3. ✅ ARREGLO: Restablecemos el nombre del perfil a un valor genérico.
        // Usamos el ID para asegurar que sea único, ej: "Perfil 25".
        perfil.setNombrePerfil("Perfil " + perfil.getId());

        // 4. Guardamos los cambios (esto no cambia)
        perfilRepository.save(perfil);
    }












    // --- ✅ NUEVOS MÉTODOS PARA REPORTE Y REEMPLAZO ---

    public CuentaDTO reportarCuenta(Long cuentaId, ReportarCuentaDTO dto) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta a reportar no encontrada, id: " + cuentaId));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario que reporta no encontrado, id: " + dto.getUsuarioId()));

        if (dto.isMarcarComoVencida()) {
            if (cuenta.getStatus() != StatusCuenta.ACTIVO) {
                throw new IllegalStateException("Solo se puede marcar como REPORTADO una cuenta que está en estado ACTIVO.");
            }
            cuenta.setStatus(StatusCuenta.REPORTADO);
        }

        ReporteCuenta reporte = new ReporteCuenta();
        reporte.setCuenta(cuenta);
        reporte.setUsuario(usuario);
        reporte.setFecha(LocalDate.now());
        reporte.setMotivo(dto.getMotivo());
        reporte.setDetalle(dto.getDetalle());
        reporteCuentaRepository.save(reporte);

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return convertToDTO(cuentaActualizada);
    }

    public HistorialCuentaDTO reemplazarCuentaCompleta(Long cuentaVencidaId, ReemplazarCuentaDTO dto) {
        Cuenta cuentaVencida = cuentaRepository.findById(cuentaVencidaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta vencida no encontrada, id: " + cuentaVencidaId));
        Cuenta cuentaNueva = cuentaRepository.findById(dto.getCuentaNuevaId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta nueva no encontrada, id: " + dto.getCuentaNuevaId()));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado, id: " + dto.getUsuarioId()));

        if (cuentaVencida.getStatus() != StatusCuenta.REPORTADO || cuentaVencida.getTipoCuenta() != TipoCuenta.COMPLETO) {
            throw new IllegalStateException("La cuenta a reemplazar debe estar REPORTADO y ser de tipo COMPLETO.");
        }
        if (cuentaNueva.getStatus() != StatusCuenta.SINUSAR || cuentaNueva.getCliente() != null) {
            throw new IllegalStateException("La cuenta nueva debe estar SIN USAR y sin cliente asignado.");
        }

        cuentaNueva.setCliente(cuentaVencida.getCliente());
        cuentaNueva.setFechaInicio(cuentaVencida.getFechaInicio());
        cuentaNueva.setFechaRenovacion(cuentaVencida.getFechaRenovacion());
        cuentaNueva.setPrecioVenta(cuentaVencida.getPrecioVenta());
        cuentaNueva.setStatus(StatusCuenta.ACTIVO);

        cuentaVencida.setCliente(null);
        cuentaVencida.setStatus(StatusCuenta.REEMPLAZADA);

        cuentaRepository.save(cuentaVencida);
        cuentaRepository.save(cuentaNueva);

        HistorialCuenta historial = new HistorialCuenta();
        historial.setCuentaAnterior(cuentaVencida);
        historial.setCuentaNueva(cuentaNueva);
        historial.setMotivo(dto.getMotivo());
        historial.setFechaCambio(LocalDateTime.now());
        historial.setUsuario(usuario);
        historialCuentaRepository.save(historial);

        return convertHistorialToDTO(historial);
    }

    public HistorialCuentaDTO reemplazarCuentaIndividual(Long cuentaVencidaId, ReemplazarCuentaDTO dto) {
        Cuenta cuentaVencida = cuentaRepository.findById(cuentaVencidaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta vencida no encontrada, id: " + cuentaVencidaId));
        Cuenta cuentaNueva = cuentaRepository.findById(dto.getCuentaNuevaId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta nueva no encontrada, id: " + dto.getCuentaNuevaId()));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado, id: " + dto.getUsuarioId()));

        if (cuentaVencida.getStatus() != StatusCuenta.REPORTADO || cuentaVencida.getTipoCuenta() != TipoCuenta.INDIVIDUAL) {
            throw new IllegalStateException("La cuenta a reemplazar debe estar REPORTADA y ser de tipo INDIVIDUAL.");
        }
        if (cuentaNueva.getStatus() != StatusCuenta.SINUSAR || !cuentaNueva.getPerfilesAsignados().stream().allMatch(p -> p.getCliente() == null)) {
            throw new IllegalStateException("La cuenta nueva debe estar SIN USAR y con todos sus perfiles libres.");
        }
        if (cuentaNueva.getPerfilesMaximos() < cuentaVencida.getPerfilesAsignados().size()) {
            throw new IllegalStateException("La cuenta nueva no tiene suficientes perfiles para el reemplazo.");
        }

        List<Perfil> perfilesAMover = new ArrayList<>(cuentaVencida.getPerfilesAsignados());
        cuentaVencida.getPerfilesAsignados().clear();

        for (Perfil perfil : perfilesAMover) {
            perfil.setCuenta(cuentaNueva);
        }
        cuentaNueva.getPerfilesAsignados().clear();
        cuentaNueva.getPerfilesAsignados().addAll(perfilesAMover);

        cuentaNueva.setStatus(StatusCuenta.ACTIVO);
        cuentaVencida.setStatus(StatusCuenta.REEMPLAZADA);

        cuentaRepository.save(cuentaVencida);
        cuentaRepository.save(cuentaNueva);

        HistorialCuenta historial = new HistorialCuenta();
        historial.setCuentaAnterior(cuentaVencida);
        historial.setCuentaNueva(cuentaNueva);
        historial.setMotivo(dto.getMotivo());
        historial.setFechaCambio(LocalDateTime.now());
        historial.setUsuario(usuario);
        historialCuentaRepository.save(historial);

        return convertHistorialToDTO(historial);
    }








    /**
     * Busca y devuelve todos los perfiles individuales que ya han vencido.
     * Un perfil se considera vencido si tiene un cliente asignado y su
     * fecha de renovación es anterior al día de hoy.
     * @return Una lista de DTOs de los perfiles vencidos.
     */
    @Transactional(readOnly = true)
    public List<PerfilDTO> findPerfilesVencidos() {
        LocalDate hoy = LocalDate.now();

        // Usamos el nuevo método del repositorio
        List<Perfil> perfilesVencidos = perfilRepository.findByClienteIsNotNullAndFechaRenovacionBefore(hoy);

        // Convertimos las entidades a DTOs para la respuesta
        return perfilesVencidos.stream()
                .map(this::convertPerfilToDTO)
                .collect(Collectors.toList());
    }






    @Transactional(readOnly = true)
    public List<CuentaDTO> findAll() {
        return cuentaRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaDTO findById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));
        return convertToDTO(cuenta);
    }




    // --- ================================================================= ---
    // --- ✅ MÉTODO UPDATE TOTALMENTE MEJORADO Y DINÁMICO ✅ ---
    // --- ================================================================= ---
    public CuentaDTO update(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + id));

        if (cuentaDTO.getCorreo() != null && !cuenta.getCorreo().equals(cuentaDTO.getCorreo()) && cuentaRepository.findByCorreo(cuentaDTO.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con este correo");
        }

        TipoCuenta tipoOriginal = cuenta.getTipoCuenta();
        TipoCuenta tipoNuevo = cuentaDTO.getTipoCuenta();

        if (tipoOriginal == TipoCuenta.COMPLETO && tipoNuevo == TipoCuenta.INDIVIDUAL) {
            if (cuenta.getCliente() != null) {
                throw new IllegalArgumentException("No se puede cambiar una cuenta COMPLETA ya vendida a INDIVIDUAL.");
            }
        }
        if (tipoOriginal == TipoCuenta.INDIVIDUAL && tipoNuevo == TipoCuenta.COMPLETO) {
            boolean tienePerfilesVendidos = cuenta.getPerfilesAsignados().stream().anyMatch(p -> p.getCliente() != null);
            if (tienePerfilesVendidos) {
                throw new IllegalArgumentException("No se puede cambiar una cuenta INDIVIDUAL con perfiles vendidos a COMPLETA.");
            }
        }
        // ✅ NUEVA VALIDACIÓN OPCIONAL:
        // Asegura que una cuenta INDIVIDUAL nunca reciba un clienteId a nivel de cuenta.
        if (cuentaDTO.getTipoCuenta() == TipoCuenta.INDIVIDUAL && cuentaDTO.getClienteId() != null) {
            throw new IllegalArgumentException("Una cuenta de tipo INDIVIDUAL no puede tener un clienteId principal. Asigne el cliente a través de un perfil.");
        }
        mapDtoToEntity(cuentaDTO, cuenta);
        gestionarPerfilesTrasCambioDeTipo(cuenta, tipoOriginal, tipoNuevo);
        Cuenta updated = cuentaRepository.save(cuenta);

        return convertToDTO(updated);
    }



    public void delete(Long id) {
        if (!cuentaRepository.existsById(id)) {
            throw new EntityNotFoundException("Cuenta no encontrada con id: " + id);
        }
        cuentaRepository.deleteById(id);
    }

    public VentaCuentaDTO asignarCuenta(AsignarCuentaDTO asignarDTO) {
        Cuenta cuenta = cuentaRepository.findById(asignarDTO.getCuentaId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con id: " + asignarDTO.getCuentaId()));
        Cliente cliente = clienteRepository.findById(asignarDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + asignarDTO.getClienteId()));
        Usuario usuarioAsignador = usuarioRepository.findById(asignarDTO.getUsuarioAsignadorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario asignador no encontrado con id: " + asignarDTO.getUsuarioAsignadorId()));

        // --- LÓGICA DE VALIDACIÓN CORREGIDA ---
        // La condición ahora comprueba correctamente si la cuenta ya tiene un cliente
        // O si alguno de sus perfiles ya está asignado.
        if (cuenta.getCliente() != null || cuenta.getPerfilesAsignados().stream().anyMatch(p -> p.getCliente() != null)) {
            throw new IllegalArgumentException("Esta cuenta ya fue vendida o tiene perfiles asignados.");
        }

        if (cuenta.getTipoCuenta() != TipoCuenta.COMPLETO) {
            throw new IllegalArgumentException("Este método solo es para asignar cuentas de tipo COMPLETO.");
        }

        cuenta.setCliente(cliente);
        cuenta.setStatus(StatusCuenta.ACTIVO);
        cuenta.setFechaInicio(LocalDate.now());
        cuenta.setFechaRenovacion(LocalDate.now().plusDays(30));
        cuenta.setPrecioVenta(asignarDTO.getPrecioVenta());
        cuentaRepository.save(cuenta);

        // Esta llamada ahora funcionará porque encontrará el método de 4 argumentos.
        VentaCuenta venta = registrarVenta(cuenta, cliente, asignarDTO.getPrecioVenta(), usuarioAsignador);
        return convertVentaToDTO(venta);
    }



    /**
     * Renueva una cuenta COMPLETA que estaba VENCIDA.
     * La reactiva, actualiza sus fechas y registra una nueva venta.
     * @param cuentaId El ID de la cuenta vencida a renovar.
     * @param dto El DTO con el nuevo precio y el usuario que renueva.
     * @return El DTO de la cuenta ya renovada y activa.
     */
    public CuentaDTO renovarSuscripcionCompleta(Long cuentaId, RenovarSuscripcionDTO dto) {
        // 1. Validar entidades
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada, id: " + cuentaId));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado, id: " + dto.getUsuarioId()));

        // 2. Validar estado
        if (cuenta.getStatus() != StatusCuenta.VENCIDO) {
            throw new IllegalStateException("Solo se pueden renovar cuentas en estado VENCIDO.");
        }
        if (cuenta.getTipoCuenta() != TipoCuenta.COMPLETO || cuenta.getCliente() == null) {
            throw new IllegalStateException("La cuenta debe ser de tipo COMPLETO y tener un cliente asignado.");
        }

        // 3. Actualizar cuenta
        cuenta.setStatus(StatusCuenta.ACTIVO);
        cuenta.setFechaInicio(LocalDate.now());
        cuenta.setFechaRenovacion(LocalDate.now().plusDays(30));
        cuenta.setPrecioVenta(dto.getNuevoPrecioVenta());

        // 4. ✅ Registrar la renovación como una nueva venta
        registrarVenta(cuenta, cuenta.getCliente(), dto.getNuevoPrecioVenta(), usuario);

        // 5. Guardar y devolver
        Cuenta cuentaRenovada = cuentaRepository.save(cuenta);
        return convertToDTO(cuentaRenovada);
    }

    /**
     * Renueva un PERFIL de una cuenta INDIVIDUAL que estaba VENCIDO.
     * Lo reactiva, actualiza sus fechas y registra una nueva venta para ese perfil.
     * @param perfilId El ID del perfil vencido a renovar.
     * @param dto El DTO con el nuevo precio y el usuario que renueva.
     * @return El DTO del perfil ya renovado y activo.
     */
    public PerfilDTO renovarSuscripcionPerfil(Long perfilId, RenovarSuscripcionDTO dto) {
        // 1. Validar entidades
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado, id: " + perfilId));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado, id: " + dto.getUsuarioId()));

        // 2. Validar estado (Nota: el estado VENCIDO está en la Cuenta padre)
        Cuenta cuentaPadre = perfil.getCuenta();
        if (cuentaPadre.getStatus() != StatusCuenta.VENCIDO && cuentaPadre.getStatus() != StatusCuenta.ACTIVO) {
            throw new IllegalStateException("Solo se pueden renovar perfiles de cuentas VENCIDAS o ACTIVAS.");
        }
        if (perfil.getCliente() == null) {
            throw new IllegalStateException("El perfil debe tener un cliente asignado para ser renovado.");
        }

        // 3. Actualizar perfil
        perfil.setFechaInicio(LocalDate.now());
        perfil.setFechaRenovacion(LocalDate.now().plusDays(30));
        perfil.setPrecioVenta(dto.getNuevoPrecioVenta());

        // Si la cuenta padre estaba vencida, la reactivamos
        if (cuentaPadre.getStatus() == StatusCuenta.VENCIDO) {
            cuentaPadre.setStatus(StatusCuenta.ACTIVO);
            cuentaRepository.save(cuentaPadre);
        }

        // 4. ✅ Registrar la renovación como una nueva venta de perfil
        registrarVenta(cuentaPadre, perfil, perfil.getCliente(), dto.getNuevoPrecioVenta(), usuario);

        // 5. Guardar y devolver
        Perfil perfilRenovado = perfilRepository.save(perfil);
        return convertPerfilToDTO(perfilRenovado);
    }




    // --- Métodos de Lote, Vencer y Buscar (sin cambios) ---
    public List<VentaCuentaDTO> asignarLote(AsignarLoteDTO asignarLoteDTO) { return new ArrayList<>(); }

    public List<CuentaDTO> vencerAutomatico() { return new ArrayList<>(); }

    public List<CuentaDTO> buscarCuentas(StatusCuenta status, Long servicioId, Long clienteId) { return new ArrayList<>(); }

    /**
     * Mapea los datos de un CuentaDTO a una entidad Cuenta.
     * Se ha eliminado la asignación de cliente desde aquí para mayor seguridad.
     */
    private void mapDtoToEntity(CuentaDTO dto, Cuenta entity) {
        entity.setCorreo(dto.getCorreo());
        entity.setContraseña(dto.getContraseña());
        entity.setPin(dto.getPin());
        entity.setPerfilesMaximos(dto.getPerfilesMaximos());
        entity.setEnlace(dto.getEnlace());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaRenovacion(dto.getFechaRenovacion());
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getTipoCuenta() != null) {
            entity.setTipoCuenta(dto.getTipoCuenta());
        }
        entity.setPrecioVenta(dto.getPrecioVenta());
        // ✅ Se quita la asignación de clienteId. Eso se hace en asignarCuenta().
    }

    /**
     * ✅ Nuevo método privado para centralizar la lógica de creación y eliminación de perfiles.
     * Es llamado por create() y update() para mantener el código limpio y sin repetición.
     */
    private void gestionarPerfilesTrasCambioDeTipo(Cuenta cuenta, TipoCuenta tipoOriginal, TipoCuenta tipoNuevo) {
        if (tipoNuevo == TipoCuenta.INDIVIDUAL) {
            boolean esNuevaOConversion = tipoOriginal != TipoCuenta.INDIVIDUAL;
            if (esNuevaOConversion && cuenta.getPerfilesMaximos() != null && cuenta.getPerfilesMaximos() > 0) {

                // ✅ LA CORRECCIÓN ESTÁ AQUÍ: Se modifica la lista existente en lugar de reemplazarla
                cuenta.getPerfilesAsignados().clear(); // Vaciamos la lista actual

                List<Perfil> perfilesNuevos = new ArrayList<>();
                for (int i = 1; i <= cuenta.getPerfilesMaximos(); i++) {
                    Perfil perfil = new Perfil();
                    perfil.setNombrePerfil("Perfil " + i);
                    perfil.setCuenta(cuenta);
                    perfilesNuevos.add(perfil);
                }

                cuenta.getPerfilesAsignados().addAll(perfilesNuevos); // Añadimos los nuevos a la lista existente
            }
        } else if (tipoOriginal == TipoCuenta.INDIVIDUAL && tipoNuevo != TipoCuenta.INDIVIDUAL) {
            cuenta.getPerfilesAsignados().clear();
        }
    }



    private PerfilDTO convertPerfilToDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setNombrePerfil(perfil.getNombrePerfil());

        // Obtiene datos de la Cuenta padre
        if (perfil.getCuenta() != null) {
            dto.setCorreoCuenta(perfil.getCuenta().getCorreo());

            // ✅ Se obtiene la contraseña de la Cuenta padre
            dto.setContraseña(perfil.getCuenta().getContraseña());

            // ✅ Se obtiene el PIN de la Cuenta padre
            dto.setPin(perfil.getCuenta().getPin());

            if (perfil.getCuenta().getServicio() != null) {
                dto.setUrlImg(perfil.getCuenta().getServicio().getUrlImg());
            }
        }

        // Obtiene datos del Cliente asignado (si existe)
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


    private CuentaDTO convertToDTO(Cuenta cuenta) {
        long perfilesOcupados = 0;
        if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL) {
            perfilesOcupados = cuenta.getPerfilesAsignados().stream()
                    .filter(p -> p.getCliente() != null)
                    .count();
        }

        return new CuentaDTO(
                cuenta.getId(), cuenta.getCorreo(), cuenta.getContraseña(), cuenta.getPin(),
                cuenta.getPerfilesMaximos(), (int) perfilesOcupados, cuenta.getEnlace(),
                cuenta.getFechaInicio(), cuenta.getFechaRenovacion(), cuenta.getStatus(),
                cuenta.getTipoCuenta(), cuenta.getPrecioVenta(),
                cuenta.getCliente() != null ? cuenta.getCliente().getId() : null,
                cuenta.getServicio() != null ? cuenta.getServicio().getId() : null
        );
    }

    /**
     * Registra la venta de una CUENTA COMPLETA.
     * No está asociado a ningún perfil específico.
     */
    private VentaCuenta registrarVenta(Cuenta cuenta, Cliente cliente, BigDecimal precioVenta, Usuario usuarioAsignador) {
        VentaCuenta venta = new VentaCuenta();
        venta.setCuenta(cuenta);
        venta.setCliente(cliente);
        venta.setPrecioVenta(precioVenta);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTipoCliente(cliente.getTipoCliente());
        venta.setUsuarioAsignador(usuarioAsignador);
        return ventaCuentaRepository.save(venta);
    }

    /**
     * Registra la venta de un PERFIL INDIVIDUAL.
     * Crea un vínculo directo en la venta al perfil específico que se vendió.
     */
    private VentaCuenta registrarVenta(Cuenta cuenta, Perfil perfil, Cliente cliente, BigDecimal precioVenta, Usuario usuarioAsignador) {
        VentaCuenta venta = new VentaCuenta();
        venta.setCuenta(cuenta);
        venta.setPerfil(perfil); // <-- Se añade la referencia al perfil específico
        venta.setCliente(cliente);
        venta.setPrecioVenta(precioVenta);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTipoCliente(cliente.getTipoCliente());
        venta.setUsuarioAsignador(usuarioAsignador);
        return ventaCuentaRepository.save(venta);
    }

    private VentaCuentaDTO convertVentaToDTO(VentaCuenta venta) {
        VentaCuentaDTO dto = new VentaCuentaDTO();
        dto.setId(venta.getId());
        dto.setCuentaId(venta.getCuenta().getId());
        dto.setClienteId(venta.getCliente().getId());
        dto.setPrecioVenta(venta.getPrecioVenta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTipoCliente(venta.getTipoCliente());
        dto.setUsuarioAsignadorId(venta.getUsuarioAsignador().getId());

        // --- CAMBIO CLAVE ---
        if (venta.getPerfil() != null) {
            dto.setPerfilId(venta.getPerfil().getId());
        }
        return dto;
    }

    private HistorialCuentaDTO convertHistorialToDTO(HistorialCuenta historial) {
        HistorialCuentaDTO dto = new HistorialCuentaDTO();
        dto.setId(historial.getId());
        dto.setCuentaAnteriorId(historial.getCuentaAnterior().getId());
        dto.setCuentaNuevaId(historial.getCuentaNueva().getId());
        dto.setMotivo(historial.getMotivo());
        dto.setFechaCambio(historial.getFechaCambio());
        dto.setUsuarioId(historial.getUsuario().getId());
        return dto;
    }

    // --- MÉTODO NUEVO PARA REPORTES (EL QUE FALTABA) ---
    /**
     * Genera un reporte de estado y asignación para todas las cuentas del sistema.
     * @return Una lista de DTOs, cada uno representando el estado de una cuenta.
     */
    @Transactional(readOnly = true)
    public List<ReporteAsignacionDTO> getReporteDeAsignaciones() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentas.stream()
                .map(this::convertCuentaToReporteDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar privado para convertir una entidad Cuenta en su DTO de reporte.
     * @param cuenta La entidad Cuenta a convertir.
     * @return El DTO de reporte correspondiente.
     */
    private ReporteAsignacionDTO convertCuentaToReporteDTO(Cuenta cuenta) {
        ReporteAsignacionDTO dto = new ReporteAsignacionDTO();
        dto.setCuentaId(cuenta.getId());
        dto.setCorreoCuenta(cuenta.getCorreo());
        dto.setTipoCuenta(cuenta.getTipoCuenta());

        if (cuenta.getTipoCuenta() == TipoCuenta.COMPLETO) {
            // Si es COMPLETA, se informa el cliente asignado a la cuenta principal
            if (cuenta.getCliente() != null) {
                dto.setClienteAsignadoId(cuenta.getCliente().getId());
                dto.setNombreClienteAsignado(cuenta.getCliente().getNombre());
            }
            dto.setFechaInicioCuenta(cuenta.getFechaInicio());
            dto.setFechaRenovacionCuenta(cuenta.getFechaRenovacion());

        } else if (cuenta.getTipoCuenta() == TipoCuenta.INDIVIDUAL) {
            // Si es INDIVIDUAL, se informa la lista de perfiles que SÍ tienen cliente
            dto.setPerfilesMaximos(cuenta.getPerfilesMaximos());
            List<PerfilDTO> perfilesAsignadosDTO = cuenta.getPerfilesAsignados().stream()
                    .filter(perfil -> perfil.getCliente() != null) // Solo perfiles ocupados
                    .map(this::convertPerfilToPerfilDTO)
                    .collect(Collectors.toList());
            dto.setPerfilesAsignados(perfilesAsignadosDTO);
        }

        return dto;
    }

    /**
     * Método auxiliar privado para convertir una entidad Perfil a su DTO.
     * @param perfil La entidad Perfil a convertir.
     * @return El PerfilDTO correspondiente.
     */
    private PerfilDTO convertPerfilToPerfilDTO(Perfil perfil) {
        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setNombrePerfil(perfil.getNombrePerfil());
        if (perfil.getCliente() != null) {
            dto.setClienteId(perfil.getCliente().getId());
            dto.setNombreCliente(perfil.getCliente().getNombre());
        }
        dto.setFechaInicio(perfil.getFechaInicio());
        dto.setFechaRenovacion(perfil.getFechaRenovacion());
        dto.setPrecioVenta(perfil.getPrecioVenta());
        return dto;
    }
}