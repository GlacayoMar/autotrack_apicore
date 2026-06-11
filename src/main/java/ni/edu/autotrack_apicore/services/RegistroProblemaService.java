package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.RegistroProblema;

import java.util.List;

public interface RegistroProblemaService {
    RegistroProblema reportar(Long vehiculoId, RegistroProblema problema);

    List<RegistroProblema> listarPorVehiculo(Long vehiculoId, boolean soloActivos);

    RegistroProblema obtenerPorId(Long id);

    // Método para marcar un problema como "Solucionado" (activo = false)
    void solucionarProblema(Long id);

    // Verifica si un vehículo tiene permitido circular en base a sus problemas graves activos
    boolean esVehiculoAptoParaCircular(Long vehiculoId);
}
