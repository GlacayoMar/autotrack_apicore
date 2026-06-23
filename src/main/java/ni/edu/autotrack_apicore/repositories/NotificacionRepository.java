package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
    List<Notificacion> findByDocumentoId(Long documentoId);
    List<Notificacion> findByUsuarioIdAndEnviadaAndIgnorar(Long usuarioId, Boolean enviada, Boolean ignorar);
}
