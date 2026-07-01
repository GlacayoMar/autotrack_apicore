package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Registro;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    // Trae todo el historial del carro (combustible y problemas juntos) del más nuevo al más viejo
    List<Registro> findByVehiculoIdOrderByFechaRegistroDesc(Long vehiculoId);

    @Query("SELECT r FROM Registro r WHERE r.fechaActualizacion > :fecha")
    List<Registro> findUpdatedAfter(@Param("fecha") LocalDateTime fecha);

    // Permite filtrar el historial de un carro por un rango de fechas
    List<Registro> findByVehiculoIdAndFechaRegistroBetweenOrderByFechaRegistroDesc(
            Long vehiculoId, LocalDate inicio, LocalDate fin);

    List<RegistroProblema> findByVehiculoIdAndActivoTrue(Long vehiculoId);
}
