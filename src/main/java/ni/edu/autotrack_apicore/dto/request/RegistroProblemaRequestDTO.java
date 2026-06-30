package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.models.enums.TipoProblema;

import java.time.LocalDate;

@Data
public class RegistroProblemaRequestDTO {
    private Boolean afectaVehiculo;
    private TipoProblema tipoProblema;
    private String nota;
    private LocalDate fechaRegistro;
}
