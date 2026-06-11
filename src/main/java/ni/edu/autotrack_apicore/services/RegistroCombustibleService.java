package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.RegistroCombustible;

import java.math.BigDecimal;
import java.util.List;

public interface RegistroCombustibleService {
    RegistroCombustible crear(Long vehiculoId, RegistroCombustible registro);

    List<RegistroCombustible> listarPorVehiculo(Long vehiculoId);

    RegistroCombustible obtenerPorId(Long id);

    BigDecimal obtenerTotalGastado(Long vehiculoId);

    // Calcula los kilómetros por galón/litro basándose en el último registro del odómetro
    double calcularRendimientoPromedio(Long vehiculoId);
}
