package ni.edu.autotrack_apicore.dto.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import ni.edu.autotrack_apicore.models.Documento;
import ni.edu.autotrack_apicore.models.Usuario;
import ni.edu.autotrack_apicore.models.enums.TipoNotificacion;

import java.time.LocalDate;

@Data
public class NotificacionRequestDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private String frecuencia;
    private Boolean ignorar;
    private String mensaje;
    private Boolean enviada;
    private TipoNotificacion tipo;
}
