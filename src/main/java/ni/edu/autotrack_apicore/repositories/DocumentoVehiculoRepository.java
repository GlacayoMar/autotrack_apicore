package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentoVehiculoRepository extends JpaRepository<DocumentoVehiculo, Long> {
    List<DocumentoVehiculo> findByVehiculoId(Long vehiculoId);

    @Query(value = "SELECT dv.*, d.* FROM documentos_vehiculos dv " +
            "INNER JOIN documentos d ON dv.id = d.id" +
            " WHERE d.fecha_actualizacion > :fecha", nativeQuery = true)
    List<DocumentoVehiculo> findUpdatedAfterRaw(LocalDateTime fecha);

}
