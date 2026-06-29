package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RegistroCombustibleRequestDTO {
    private BigDecimal cantidadCombustible;
    private BigDecimal cantidadPagado;
    private Long odometro;
    private String nota;
    private LocalDate fechaRegistro;
}
