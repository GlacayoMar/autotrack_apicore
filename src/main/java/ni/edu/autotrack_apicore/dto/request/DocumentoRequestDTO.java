package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentoRequestDTO {
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmitida;
    private String imagen;
}
