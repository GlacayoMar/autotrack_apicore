package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Registro;
import java.time.LocalDate;
import java.util.List;

public interface RegistroService {
    Registro guardar(Registro registro);

    Registro obtenerPorId(Long id);

    List<Registro> listarTodoPorVehiculo(Long vehiculoId);

    List<Registro> listarPorVehiculoYRangoFechas(Long vehiculoId, LocalDate inicio, LocalDate fin);

    void eliminar(Long id);
}
