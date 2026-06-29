package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.enums.CategoriaLicencia;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LicenciaResponseDTO extends DocumentoResponseDTO {
    private Set<CategoriaLicencia> categorias;
    private Usuario usuario;
}
