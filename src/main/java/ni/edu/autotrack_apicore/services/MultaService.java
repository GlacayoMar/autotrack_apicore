package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Multa;

import java.util.List;

public interface MultaService {
    Multa crear(Long usuarioId, Multa multa);
    Multa obtenerPorId(Long id);
    List<Multa> listar();
    List<Multa> listarPorUsuarioId(Long usuarioId);
    List<Multa> listarPorUsuarioIdYEstadoPago(Long usuarioId, Boolean pagada);
    Multa actualizar(Long id, Multa multa);
    void eliminar(Long id);
    Multa pagarMulta(Long id);
}
