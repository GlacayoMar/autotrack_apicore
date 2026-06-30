package ni.edu.autotrack_apicore.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ni.edu.autotrack_apicore.models.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultaResponseDTO extends DocumentoResponseDTO {
    private String descripcion;
    private BigDecimal monto;
    private LocalDate fechaMulta;
    private LocalDate fechaLimite;
    private Boolean pagada;
    private Long usuarioId;
}
