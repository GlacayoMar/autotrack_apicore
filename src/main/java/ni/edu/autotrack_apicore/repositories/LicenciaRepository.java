package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    Optional<Licencia> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}
