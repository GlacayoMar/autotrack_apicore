package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByFechaVencimientoBefore(LocalDate fecha);

    @Query("SELECT d FROM Documento d WHERE d.fechaActualizacion > :fecha")
    List<Documento> findUpdatedAfter(LocalDateTime fecha);
}
