package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.VehiculoResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehiculoSyncDTO extends VehiculoResponseDTO {
    private Boolean eliminado;
}
