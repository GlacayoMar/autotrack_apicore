package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    Optional<Licencia> findByUsuarioId(Long usuarioId);

    @Query("SELECT l FROM Licencia l WHERE l.fechaActualizacion > :fecha")
    List<Licencia> findUpdatedAfter(@Param("fecha") LocalDateTime fecha);

    boolean existsByUsuarioId(Long usuarioId);
}
