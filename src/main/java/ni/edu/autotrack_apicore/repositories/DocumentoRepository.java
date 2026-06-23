package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByFechaVencimientoBefore(LocalDate fecha);
}
