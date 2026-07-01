package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Notificacion;
import ni.edu.autotrack_apicore.models.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
    List<Notificacion> findByDocumentoId(Long documentoId);
    List<Notificacion> findByUsuarioIdAndEnviadaAndIgnorar(Long usuarioId, Boolean enviada, Boolean ignorar);
    @Query("SELECT n FROM Notificacion n WHERE n.fechaActualizacion > :fecha")
    List<Notificacion> findUpdatedAfter(@Param("fecha") LocalDateTime fecha);
}
