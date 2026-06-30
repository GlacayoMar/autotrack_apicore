package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByVin(String vin);

    Optional<Vehiculo> findByPlaca(String placa);

    List<Vehiculo> findByUsuarioId(Long usuarioId);

    @Query(value = "SELECT * FROM vehiculos WHERE fecha_actualizacion > :fecha", nativeQuery = true)
    List<Vehiculo> findUpdatedAfterRaw(@Param("fecha") LocalDateTime fecha);

    boolean existsByVin(String vin);

    boolean existsByPlaca(String placa);
}
