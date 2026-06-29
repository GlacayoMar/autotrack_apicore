package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.models.RegistroCombustible;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistroCombustibleResponseDTO extends RegistroResponseDTO {
    private BigDecimal cantidadCombustible;
    private BigDecimal cantidadPagado;
    private Long odometro;
}
