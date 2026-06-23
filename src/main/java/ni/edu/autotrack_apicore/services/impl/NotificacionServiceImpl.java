package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.models.Notificacion;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.repositories.NotificacionRepository;
import ni.edu.autotrack_apicore.services.DocumentoService;
import ni.edu.autotrack_apicore.services.NotificacionService;
import ni.edu.autotrack_apicore.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;
    private final DocumentoService documentoService;

    @Override
    public Notificacion crear(Long usuarioId, Long documentoId, Notificacion notificacion) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Documento documento = documentoService.obtenerPorId(documentoId);
        notificacion.setUsuario(usuario);
        notificacion.setDocumento(documento);
        if (notificacion.getEnviada() == null) {
            notificacion.setEnviada(false);
        }
        if (notificacion.getIgnorar() == null) {
            notificacion.setIgnorar(false);
        }
        return notificacionRepository.save(notificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Notificacion obtenerPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuarioId(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaInicioDesc(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listarPorDocumentoId(Long documentoId) {
        return notificacionRepository.findByDocumentoId(documentoId);
    }

    @Override
    public Notificacion actualizar(Long id, Notificacion notificacionActualizada) {
        Notificacion notificacion = obtenerPorId(id);
        notificacion.setFechaInicio(notificacionActualizada.getFechaInicio());
        notificacion.setFechaFinal(notificacionActualizada.getFechaFinal());
        notificacion.setFrecuencia(notificacionActualizada.getFrecuencia());
        if (notificacionActualizada.getIgnorar() != null) {
            notificacion.setIgnorar(notificacionActualizada.getIgnorar());
        }
        notificacion.setMensaje(notificacionActualizada.getMensaje());
        if (notificacionActualizada.getEnviada() != null) {
            notificacion.setEnviada(notificacionActualizada.getEnviada());
        }
        notificacion.setTipo(notificacionActualizada.getTipo());
        return notificacionRepository.save(notificacion);
    }

    @Override
    public void eliminar(Long id) {
        Notificacion notificacion = obtenerPorId(id);
        notificacionRepository.delete(notificacion);
    }

    @Override
    public Notificacion marcarComoEnviada(Long id) {
        Notificacion notificacion = obtenerPorId(id);
        notificacion.setEnviada(true);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion marcarComoIgnorada(Long id, Boolean ignorar) {
        Notificacion notificacion = obtenerPorId(id);
        notificacion.setIgnorar(ignorar != null ? ignorar : true);
        return notificacionRepository.save(notificacion);
    }
}
