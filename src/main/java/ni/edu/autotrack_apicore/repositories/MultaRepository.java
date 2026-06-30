package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByUsuarioId(Long usuarioId);
    List<Multa> findByUsuarioIdAndPagada(Long usuarioId, Boolean pagada);
    List<Multa> findByUsuarioIdOrderByFechaMultaDesc(Long usuarioId);

    @Query(value = "SELECT m.*, d.*, 'Multa' as clazz_ FROM multas m " + // <-- Añadido espacio y 'Multa' as clazz_
            "INNER JOIN documentos d ON m.id = d.id " +           // <-- Añadido espacio al final
            "WHERE d.fecha_actualizacion > :fecha", nativeQuery = true
    )
    List<Multa> findUpdatedAfter(@Param("fecha") LocalDateTime fecha);
}
