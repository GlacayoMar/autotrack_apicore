package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Notificacion;
import ni.edu.autotrack_apicore.models.ServicioMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServicioMantenimientoRepository extends JpaRepository<ServicioMantenimiento, Long> {
    List<ServicioMantenimiento> findByVehiculoId(Long vehiculoId);

    @Query("SELECT s FROM ServicioMantenimiento s WHERE s.fechaActualizacion > :fecha")
    List<ServicioMantenimiento> findUpdatedAfter(@Param("fecha") LocalDateTime fecha);
}
