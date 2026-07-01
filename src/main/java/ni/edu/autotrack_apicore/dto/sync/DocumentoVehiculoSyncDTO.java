package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.DocumentoVehiculoResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentoVehiculoSyncDTO extends DocumentoVehiculoResponseDTO {
    private Boolean eliminado;
}
