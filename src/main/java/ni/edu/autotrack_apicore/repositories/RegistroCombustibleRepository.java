package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.RegistroCombustible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroCombustibleRepository extends JpaRepository<RegistroCombustible,Long> {
    List<RegistroCombustible> findByVehiculoId(Long vehiculoId);
}
