package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByVin(String vin);

    Optional<Vehiculo> findByPlaca(String placa);

    List<Vehiculo> findByUsuarioId(Long usuarioId);

    boolean existsByVin(String vin);

    boolean existsByPlaca(String placa);
}
