package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.TipoMantenimiento;

@Data
public class ServicioMantenimientoRequestDTO {
    private String titulo;
    private String descripcion;
    private Boolean afectaVehiculo;
    private Boolean completado;
    private Integer distanciAgendada;
    private String observaciones;
    private TipoMantenimiento tipoMantenimiento;
    private Long vehiculoId;
}
