package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class DocumentoVehiculoRequestDTO {
    private String nombre;
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmitida;
    private String imagen;

}
