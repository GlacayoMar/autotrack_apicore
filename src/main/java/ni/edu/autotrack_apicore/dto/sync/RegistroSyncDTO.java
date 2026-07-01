package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.RegistroResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroSyncDTO extends RegistroResponseDTO {
    private Boolean eliminado;
}
