package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.RegistroCombustible;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.RegistroCombustibleRepository;
import ni.edu.autotrack_apicore.services.RegistroCombustibleService;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistroCombustibleServiceImpl implements RegistroCombustibleService {
    private final RegistroCombustibleRepository combustibleRepository;
    private final VehiculoService vehiculoService;

    @Override
    @Transactional
    public RegistroCombustible crear(Long vehiculoId, RegistroCombustible registro) {

        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        registro.setVehiculo(vehiculo);
        return combustibleRepository.save(registro);
    }

    @Override
    public List<RegistroCombustible> listarPorVehiculo(Long vehiculoId) {
        return combustibleRepository.findByVehiculoIdOrderByFechaRegistroDesc(vehiculoId);
    }

    @Override
    public RegistroCombustible obtenerPorId(Long id) {
        return combustibleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de combustible no encontrado con ID: " + id));
    }

    @Override
    public BigDecimal obtenerTotalGastado(Long vehiculoId) {
        return combustibleRepository.totalGastadoPorVehiculo(vehiculoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroCombustible> listarActualizadoDespuesDe(LocalDateTime fecha) {
        return combustibleRepository.findUpdatedAfterRaw(fecha);
    }

    @Override
    @Transactional
    public RegistroCombustible actualizar(Long id, RegistroCombustible registroActualizado) {
        RegistroCombustible registro = obtenerPorId(id);
        registro.setFechaRegistro(registroActualizado.getFechaRegistro());
        registro.setNota(registroActualizado.getNota());
        registro.setOdometro(registroActualizado.getOdometro());
        registro.setCantidadCombustible(registroActualizado.getCantidadCombustible());
        registro.setCantidadPagado(registroActualizado.getCantidadPagado());
        return combustibleRepository.save(registro);
    }

    @Override
    public double calcularRendimientoPromedio(Long vehiculoId) {
        List<RegistroCombustible> registros = combustibleRepository.findByVehiculoIdOrderByFechaRegistroDesc(vehiculoId);

        if (registros.size() < 2) {
            return 0.0;
        }

        long odometroActual = registros.get(0).getOdometro();
        long odometroInicial = registros.get(registros.size() - 1).getOdometro();
        long kilometrosRecorridos = odometroActual - odometroInicial;

        BigDecimal totalCombustible = registros.stream()
                .limit(registros.size() - 1)
                .map(RegistroCombustible::getCantidadCombustible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalCombustible.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        return (double) kilometrosRecorridos / totalCombustible.doubleValue();
    }
}
