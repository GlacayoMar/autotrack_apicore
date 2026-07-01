package ni.edu.autotrack_apicore.dto.sync;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.dto.response.UsuarioResponseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsuarioSyncDTO extends UsuarioResponseDTO {
    private Boolean eliminado;
    private String password;
}
