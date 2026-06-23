package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Documento;

import java.time.LocalDate;
import java.util.List;

public interface DocumentoService {
    Documento obtenerPorId(Long id);
    List<Documento> listar();
    Documento actualizar(Long id, Documento documento);
    void eliminar(Long id);
    List<Documento> listarVencidosAntesDe(LocalDate fecha);
}
