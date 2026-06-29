package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroSyncDTO extends RegistroProblemaSyncDTO{
    private Boolean eliminado;
}
