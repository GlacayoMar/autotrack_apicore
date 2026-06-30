package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.DocumentoResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentoSyncDTO extends DocumentoResponseDTO {
    private Boolean eliminado;
}
