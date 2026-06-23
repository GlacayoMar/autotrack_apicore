package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByUsuarioId(Long usuarioId);
    List<Multa> findByUsuarioIdAndPagada(Long usuarioId, Boolean pagada);
    List<Multa> findByUsuarioIdOrderByFechaMultaDesc(Long usuarioId);
}
