package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.RegistroProblemaResponseDTO;
import ni.edu.autotrack_apicore.dto.response.RegistroResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroProblemaSyncDTO extends RegistroProblemaResponseDTO {
    private Boolean eliminado;

}
