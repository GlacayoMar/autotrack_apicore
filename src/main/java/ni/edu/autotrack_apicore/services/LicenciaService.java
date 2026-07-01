package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Licencia;

import java.time.LocalDateTime;
import java.util.List;

public interface LicenciaService {
    Licencia crear(Licencia licencia);
    Licencia obtenerPorId(Long id);
    Licencia obtenerPorUsuarioId(Long usuarioId);
    List<Licencia> listar();
    List<Licencia> listarActualizadosDespuesDe(LocalDateTime fecha);
    Licencia actualizar(Long id, Licencia licencia);
    void eliminar(Long id);
}
