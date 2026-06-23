package ni.edu.autotrack_apicore.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Registro;
import ni.edu.autotrack_apicore.repositories.RegistroRepository;
import ni.edu.autotrack_apicore.services.RegistroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository registroRepository;

    @Override
    @Transactional // Permite guardar polimórficamente cualquier hijo de Registro
    public Registro guardar(Registro registro) {
        return registroRepository.save(registro);
    }

    @Override
    public Registro obtenerPorId(Long id) {
        return registroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro general no encontrado con ID: " + id));
    }

    @Override
    public List<Registro> listarTodoPorVehiculo(Long vehiculoId) {
        return registroRepository.findByVehiculoIdOrderByFechaRegistroDesc(vehiculoId);
    }

    @Override
    public List<Registro> listarPorVehiculoYRangoFechas(Long vehiculoId, LocalDate inicio, LocalDate fin) {
        return registroRepository.findByVehiculoIdAndFechaRegistroBetweenOrderByFechaRegistroDesc(vehiculoId, inicio, fin);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Registro registro = registroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede eliminar. Registro no encontrado."));

        registroRepository.delete(registro);
    }
}
