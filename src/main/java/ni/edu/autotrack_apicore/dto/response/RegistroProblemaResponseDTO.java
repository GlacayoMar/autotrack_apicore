package ni.edu.autotrack_apicore.dto.response;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.models.enums.TipoProblema;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroProblemaResponseDTO extends RegistroResponseDTO {
    private Boolean afectaVehiculo;
    private TipoProblema tipoProblema;
}
