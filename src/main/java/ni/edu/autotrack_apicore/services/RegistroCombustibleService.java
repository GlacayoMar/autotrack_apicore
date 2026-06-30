package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.RegistroCombustible;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroCombustibleService {
    RegistroCombustible crear(Long vehiculoId, RegistroCombustible registro);

    List<RegistroCombustible> listarPorVehiculo(Long vehiculoId);

    List<RegistroCombustible> listarActualizadoDespuesDe(LocalDateTime fecha);

    RegistroCombustible obtenerPorId(Long id);

    BigDecimal obtenerTotalGastado(Long vehiculoId);

    RegistroCombustible actualizar(Long id, RegistroCombustible registro);

    // Calcula los kilómetros por galón/litro basándose en el último registro del odómetro
    double calcularRendimientoPromedio(Long vehiculoId);
}
