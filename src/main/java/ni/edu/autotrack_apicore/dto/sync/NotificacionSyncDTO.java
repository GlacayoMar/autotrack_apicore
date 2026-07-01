package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.NotificacionResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotificacionSyncDTO extends NotificacionResponseDTO {
    private Boolean eliminado;
}
