package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    // Trae todo el historial del carro (combustible y problemas juntos) del más nuevo al más viejo
    List<Registro> findByVehiculoIdOrderByFechaRegistroDesc(Long vehiculoId);

    // Permite filtrar el historial de un carro por un rango de fechas
    List<Registro> findByVehiculoIdAndFechaRegistroBetweenOrderByFechaRegistroDesc(
            Long vehiculoId, LocalDate inicio, LocalDate fin);
}
