package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.ServicioMantenimiento;

import java.util.List;

public interface ServicioMantenimientoService {
    ServicioMantenimiento crear(Long vehiculoId, ServicioMantenimiento servicio);

    ServicioMantenimiento obtenerPorId(Long id);

    List<ServicioMantenimiento> listar();

    List<ServicioMantenimiento> listarPorVehiculoId(Long vehiculoId);

    ServicioMantenimiento actualizar(Long id, ServicioMantenimiento servicioActualizado);

    void eliminar(Long id);

    ServicioMantenimiento cambiarEstadoCompletado(Long id, boolean completado);
}
