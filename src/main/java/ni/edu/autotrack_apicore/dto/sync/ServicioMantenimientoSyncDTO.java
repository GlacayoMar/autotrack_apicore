package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.ServicioMantenimientoResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServicioMantenimientoSyncDTO extends ServicioMantenimientoResponseDTO {
    private Boolean eliminado;
}
