package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentoResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmitida;
    private String imagen;
}
