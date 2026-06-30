package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.ServicioMantenimiento;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.ServicioMantenimientoRepository;
import ni.edu.autotrack_apicore.repositories.VehiculoRepository;
import ni.edu.autotrack_apicore.services.ServicioMantenimientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ServicioMantenimientoImpl implements ServicioMantenimientoService{
    private final ServicioMantenimientoRepository mantenimientoRepository;

    private final VehiculoRepository vehiculoRepository;

    @Override
    public ServicioMantenimiento crear(Long vehiculoId, ServicioMantenimiento servicio) {
        // Validamos que el vehículo exista antes de asignarlo
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new EntityNotFoundException("Vehículo no encontrado con ID: " + vehiculoId));

        servicio.setVehiculo(vehiculo);

        // Estado inicial por defecto
        if (Boolean.TRUE.equals(servicio.getCompletado())) {
            servicio.setCompletado(false);
        }

        if (servicio.getFechaAgendada() != null && servicio.getFechaAgendada().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha agendada no puede ser anterior a la fecha actual");
        }

        return mantenimientoRepository.save(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioMantenimiento obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio de mantenimiento no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioMantenimiento> listar() {
        return mantenimientoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioMantenimiento> listarPorVehiculoId(Long vehiculoId) {
        return mantenimientoRepository.findByVehiculoId(vehiculoId);
    }

    @Override
    public ServicioMantenimiento actualizar(Long id, ServicioMantenimiento servicioActualizado) {
        ServicioMantenimiento existente = obtenerPorId(id);

        // Actualización de campos estructurados
        existente.setTitulo(servicioActualizado.getTitulo());
        existente.setDescripcion(servicioActualizado.getDescripcion());
        existente.setAfectaVehiculo(servicioActualizado.getAfectaVehiculo());
        existente.setCompletado(servicioActualizado.getCompletado());
        existente.setDistanciaAgendada(servicioActualizado.getDistanciaAgendada());
        existente.setObservaciones(servicioActualizado.getObservaciones());
        if (servicioActualizado.getFechaAgendada() != null) {
            // Si la nueva fecha es distinta a la que ya tenía guardada y es del pasado, la rebotamos
            if (!servicioActualizado.getFechaAgendada().equals(existente.getFechaAgendada())
                    && servicioActualizado.getFechaAgendada().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("No puedes reagendar un mantenimiento para una fecha pasada");
            }
            existente.setFechaAgendada(servicioActualizado.getFechaAgendada());
        }
        existente.setTipo(servicioActualizado.getTipo());

        return mantenimientoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        ServicioMantenimiento servicio = obtenerPorId(id);
        // SoftDelete automático de Hibernate 6 se dispara aquí cambiando 'eliminado' a true
        mantenimientoRepository.delete(servicio);
    }

    @Override
    public ServicioMantenimiento cambiarEstadoCompletado(Long id, boolean completado) {
        ServicioMantenimiento servicio = obtenerPorId(id);
        servicio.setCompletado(completado);
        return mantenimientoRepository.save(servicio);
    }
}
