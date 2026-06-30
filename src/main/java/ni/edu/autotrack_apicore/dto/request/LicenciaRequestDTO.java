package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.CategoriaLicencia;

import java.time.LocalDate;
import java.util.Set;

@Data
public class LicenciaRequestDTO {
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmitida;
    private String imagen;
    private Set<CategoriaLicencia> categorias;
    private Long usuarioId;
}
