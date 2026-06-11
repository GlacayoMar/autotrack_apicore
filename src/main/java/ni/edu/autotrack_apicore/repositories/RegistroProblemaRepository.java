package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroProblemaRepository extends JpaRepository<RegistroProblema,Long> {
    List<RegistroProblema> findByVehiculoIdOrderByFechaRegistroDesc(Long vehiculoId);

    // Obtiene solo los problemas que siguen abiertos (activo = true) de un carro
    List<RegistroProblema> findByVehiculoIdAndActivoTrue(Long vehiculoId);

    // Cuenta cuántos problemas graves e irresolutos (activos y que afectan al vehículo) tiene el carro
    long countByVehiculoIdAndActivoTrueAndAfectaVehiculoTrue(Long vehiculoId);
}
