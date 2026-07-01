package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.RegistroCombustibleResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroCombustibleSyncDTO extends RegistroCombustibleResponseDTO {
    private Boolean eliminado;

}
