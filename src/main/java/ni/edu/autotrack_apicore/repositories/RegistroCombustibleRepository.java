package ni.edu.autotrack_apicore.repositories;

import ni.edu.autotrack_apicore.models.RegistroCombustible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RegistroCombustibleRepository extends JpaRepository<RegistroCombustible,Long> {
    List<RegistroCombustible> findByVehiculoIdOrderByFechaRegistroDesc(Long vehiculoId);

    @Query("SELECT COALESCE(SUM(rc.cantidadPagado), 0) FROM RegistroCombustible rc WHERE rc.vehiculo.id = :vehiculoId")
    BigDecimal totalGastadoPorVehiculo(@Param("vehiculoId") Long vehiculoId);

    @Query("SELECT COALESCE(SUM(rc.cantidadCombustible), 0) FROM RegistroCombustible rc WHERE rc.vehiculo.id = :vehiculoId")
    BigDecimal totalCombustibleConsumidoPorVehiculo(@Param("vehiculoId") Long vehiculoId);
}
