package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroProblemaRepository extends JpaRepository<RegistroProblema,Long> {
    List<RegistroProblema> findByVehiculoId(Long vehiculoId);
    List<RegistroProblema> findByActivoTrue();
}
