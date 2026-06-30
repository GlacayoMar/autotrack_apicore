package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.ServicioMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioMantenimientoRepository extends JpaRepository<ServicioMantenimiento, Long> {
    List<ServicioMantenimiento> findByVehiculoId(Long vehiculoId);
}
