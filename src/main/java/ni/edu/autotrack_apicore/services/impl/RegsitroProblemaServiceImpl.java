package ni.edu.autotrack_apicore.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.RegistroProblema;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.RegistroProblemaRepository;
import ni.edu.autotrack_apicore.services.RegistroProblemaService;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegsitroProblemaServiceImpl implements RegistroProblemaService {

    private final RegistroProblemaRepository problemaRepository;
    private final VehiculoService vehiculoService;

    @Override
    @Transactional
    public RegistroProblema reportar(Long vehiculoId, RegistroProblema problema) {
        Vehiculo vehiculo = vehiculoService.obtenerPorId(vehiculoId);
        problema.setVehiculo(vehiculo);
        problema.setActivo(true); // Todo problema reportado inicia abierto
        return problemaRepository.save(problema);
    }

    @Override
    public List<RegistroProblema> listarPorVehiculo(Long vehiculoId, boolean soloActivos) {
        if (soloActivos) {
            return problemaRepository.findByVehiculoIdAndActivoTrue(vehiculoId);
        }
        return problemaRepository.findByVehiculoIdOrderByFechaRegistroDesc(vehiculoId);
    }

    @Override
    public List<RegistroProblema> listarActualizadoDespuesDe(LocalDateTime fecha) {
        return problemaRepository.findUpdatedAfterRaw(fecha);
    }

    @Override
    public RegistroProblema obtenerPorId(Long id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de problema no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void solucionarProblema(Long id) {
        RegistroProblema problema = obtenerPorId(id);
        problema.setActivo(false); // Cierre del reporte de daño
    }

    @Override
    @Transactional
    public RegistroProblema actualizar(Long id, RegistroProblema registroProblema) {
        RegistroProblema problema = obtenerPorId(id);
        problema.setFechaRegistro(registroProblema.getFechaRegistro());
        problema.setNota(registroProblema.getNota());
        problema.setAfectaVehiculo(registroProblema.getAfectaVehiculo());
        problema.setTipoProblema(registroProblema.getTipoProblema());
        return problemaRepository.save(problema);
    }

    @Override
    public boolean esVehiculoAptoParaCircular(Long vehiculoId) {
        // Si tiene 1 o más problemas activos que "afectan al vehículo", no es apto para andar en calle
        long problemasGraves = problemaRepository.countByVehiculoIdAndActivoTrueAndAfectaVehiculoTrue(vehiculoId);
        return problemasGraves == 0;
    }


}
