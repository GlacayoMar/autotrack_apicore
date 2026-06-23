package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Licencia;

import java.util.List;

public interface LicenciaService {
    Licencia crear(Long usuarioId, Licencia licencia);
    Licencia obtenerPorId(Long id);
    Licencia obtenerPorUsuarioId(Long usuarioId);
    List<Licencia> listar();
    Licencia actualizar(Long id, Licencia licencia);
    void eliminar(Long id);
}
