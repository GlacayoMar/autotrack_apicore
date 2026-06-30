package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.RegistroProblema;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroProblemaService {
    RegistroProblema reportar(Long vehiculoId, RegistroProblema problema);

    List<RegistroProblema> listarPorVehiculo(Long vehiculoId, boolean soloActivos);

    List<RegistroProblema> listarActualizadoDespuesDe(LocalDateTime fecha);

    RegistroProblema obtenerPorId(Long id);

    RegistroProblema actualizar(Long id, RegistroProblema problema);

    // Método para marcar un problema como "Solucionado" (activo = false)
    void solucionarProblema(Long id);

    // Verifica si un vehículo tiene permitido circular en base a sus problemas graves activos
    boolean esVehiculoAptoParaCircular(Long vehiculoId);
}
