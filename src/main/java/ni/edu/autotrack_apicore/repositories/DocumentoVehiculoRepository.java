package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.DocumentoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoVehiculoRepository extends JpaRepository<DocumentoVehiculo, Long> {
    List<DocumentoVehiculo> findByVehiculoId(Long vehiculoId);
}
