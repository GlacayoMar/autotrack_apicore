package ni.edu.autotrack_apicore.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ni.edu.autotrack_apicore.models.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MultaRequestDTO {
    private LocalDate fechaVencimiento;
    private LocalDate fechaEmitida;
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fechaMulta;
    private LocalDate fechaLimite;
    private Boolean pagada;
    private String imagen;
    private Long usuarioId;
}
