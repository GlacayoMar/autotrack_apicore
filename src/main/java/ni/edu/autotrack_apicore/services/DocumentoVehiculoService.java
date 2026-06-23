package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.DocumentoVehiculo;

import java.util.List;

public interface DocumentoVehiculoService {
    DocumentoVehiculo crear(Long vehiculoId, DocumentoVehiculo documento);
    DocumentoVehiculo obtenerPorId(Long id);
    List<DocumentoVehiculo> listar();
    List<DocumentoVehiculo> listarPorVehiculoId(Long vehiculoId);
    DocumentoVehiculo actualizar(Long id, DocumentoVehiculo documento);
    void eliminar(Long id);
}
