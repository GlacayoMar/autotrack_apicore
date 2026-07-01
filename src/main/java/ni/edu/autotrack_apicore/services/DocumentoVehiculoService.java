package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.DocumentoVehiculo;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentoVehiculoService {
    DocumentoVehiculo crear(DocumentoVehiculo documento);
    DocumentoVehiculo obtenerPorId(Long id);
    List<DocumentoVehiculo> listar();
    List<DocumentoVehiculo> listarPorVehiculoId(Long vehiculoId);
    List<DocumentoVehiculo> listarActualizadosDespuesDe(LocalDateTime fecha);
    DocumentoVehiculo actualizar(Long id, DocumentoVehiculo documento);
    void eliminar(Long id);
}
