package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.RegistroProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroProblemaRepository extends JpaRepository<RegistroProblema,Long> {
    List<RegistroProblema> findByVehiculoIdOrderByFechaRegistroDesc(Long vehiculoId);

    // Obtiene solo los problemas que siguen abiertos (activo = true) de un carro
    List<RegistroProblema> findByVehiculoIdAndActivoTrue(Long vehiculoId);

    @Query(value = "SELECT rp.*, r.* " +
            "FROM registros_problema rp " +
            "INNER JOIN registros r ON rp.id = r.id " +
            "WHERE r.fecha_actualizacion > :fecha", nativeQuery = true)
    List<RegistroProblema> findUpdatedAfterRaw(@Param("fecha") LocalDateTime fecha);

    // Cuenta cuántos problemas graves e irresolutos (activos y que afectan al vehículo) tiene el carro
    long countByVehiculoIdAndActivoTrueAndAfectaVehiculoTrue(Long vehiculoId);
}
