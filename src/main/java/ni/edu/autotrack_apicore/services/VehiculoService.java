package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.Vehiculo;

import java.time.LocalDateTime;
import java.util.List;

public interface VehiculoService {
    Vehiculo crear(Vehiculo vehiculo);

    Vehiculo obtenerPorId(Long id);

    List<Vehiculo> listar();

    List<Vehiculo> listarActualizadosDespuesDe(LocalDateTime fecha);

    Vehiculo actualizar(Long id, Vehiculo vehiculo);

    void eliminar(Long id);

    Vehiculo obtenerPorPlaca(String placa);

    Vehiculo obtenerPorVin(String vin);

    List<Vehiculo> listarPorUsuarioId(Long usuarioId);
}
