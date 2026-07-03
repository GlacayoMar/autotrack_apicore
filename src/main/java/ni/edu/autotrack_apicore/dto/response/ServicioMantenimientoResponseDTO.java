package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.TipoMantenimiento;

import java.time.LocalDateTime;

@Data
public class ServicioMantenimientoResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
    private String titulo;
    private String descripcion;
    private Boolean afectaVehiculo;
    private Boolean completado;
    private Integer distanciAgendada;
    private String observaciones;
    private TipoMantenimiento tipoMantenimiento;
    private Long vehiculoId;
    private LocalDateTime fechaAgendada;
}
