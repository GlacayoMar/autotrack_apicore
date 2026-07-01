package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentoVehiculoResponseDTO extends DocumentoResponseDTO {
    private String nombre;
    private Long vehiculoId;

}
