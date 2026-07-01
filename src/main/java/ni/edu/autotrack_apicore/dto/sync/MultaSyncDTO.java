package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.MultaResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultaSyncDTO extends MultaResponseDTO {
    private Boolean eliminado;
}
