package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    List<Registro> findByVehiculoId(Long vehiculoId);
}
