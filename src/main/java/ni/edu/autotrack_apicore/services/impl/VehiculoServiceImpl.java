package ni.edu.autotrack_apicore.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.edu.autotrack_apicore.models.Vehiculo;
import ni.edu.autotrack_apicore.repositories.VehiculoRepository;
import ni.edu.autotrack_apicore.services.VehiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {
    private final VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo crear(Vehiculo vehiculo) {

        if (vehiculoRepository.existsByPlaca(vehiculo.getPlaca())) {
            throw new IllegalArgumentException("Placa ya existe");
        }

        if (vehiculoRepository.existsByVin(vehiculo.getVin())) {
            throw new IllegalArgumentException("Vin ya existe");
        }
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo obtenerPorId(Long id) {

        return vehiculoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehiculo no encontrado"));
    }

    @Override
    public Vehiculo actualizar(Long id, Vehiculo vehiculoActualizado) {

        Vehiculo vehiculo = obtenerPorId(id);

        if (!vehiculo.getPlaca().equals(vehiculoActualizado.getPlaca())
                && vehiculoRepository.existsByPlaca(vehiculoActualizado.getPlaca())) {
            throw new IllegalArgumentException("Placa ya existente en otro vehiculo registrado");
        }

        if (!vehiculo.getVin().equals(vehiculoActualizado.getVin())
                && vehiculoRepository.existsByVin(vehiculo.getVin())) {
            throw new IllegalArgumentException("Numero de VIN/Chasis ya existe en otro vehiculo registrado");
        }

        vehiculo.setPlaca(vehiculoActualizado.getPlaca());
        vehiculo.setVin(vehiculoActualizado.getVin());
        vehiculo.setMarca(vehiculoActualizado.getMarca());
        vehiculo.setModelo(vehiculoActualizado.getModelo());
        vehiculo.setImagenes(vehiculoActualizado.getImagenes());

        return vehiculoRepository.save(vehiculo);
    }

    @Override
    @Transactional (readOnly = true)
    public List<Vehiculo> listar() {return vehiculoRepository.findAll();}

    @Override
    @Transactional(readOnly = true) // Optimizado para solo lectura
    public Vehiculo obtenerPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "No se encontró ningún vehículo registrado con la placa: " + placa));
    }

    @Override
    @Transactional(readOnly = true) // Optimizado para solo lectura
    public Vehiculo obtenerPorVin(String vin) {
        return vehiculoRepository.findByVin(vin)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "No se encontró ningún vehículo registrado con el número de VIN/Chasis: " + vin));
    }

    @Override
    @Transactional(readOnly = true) // Optimizado para solo lectura
    public List<Vehiculo> listarPorUsuarioId(Long usuarioId) {
        // Retorna la lista de carros del usuario (si no tiene ninguno, devolverá una lista vacía de forma segura)
        return vehiculoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado"));

        vehiculoRepository.delete(vehiculo);
    }
}
