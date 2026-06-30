package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.LicenciaResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class LicenciaSyncDTO extends LicenciaResponseDTO {
    private Boolean eliminado;
}
