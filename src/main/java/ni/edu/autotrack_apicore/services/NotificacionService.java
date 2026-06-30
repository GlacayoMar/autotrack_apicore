package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Notificacion;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionService {
    Notificacion crear(Long usuarioId, Long documentoId, Notificacion notificacion);
    Notificacion obtenerPorId(Long id);
    List<Notificacion> listar();
    List<Notificacion> listarPorUsuarioId(Long usuarioId);
    List<Notificacion> listarPorDocumentoId(Long documentoId);
    List<Notificacion> listarActualizadosDespuesDe(LocalDateTime fecha);
    Notificacion actualizar(Long id, Notificacion notificacion);
    void eliminar(Long id);
    Notificacion marcarComoEnviada(Long id);
    Notificacion marcarComoIgnorada(Long id, Boolean ignorar);
}
