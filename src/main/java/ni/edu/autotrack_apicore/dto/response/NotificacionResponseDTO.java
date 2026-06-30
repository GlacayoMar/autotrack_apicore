package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.TipoNotificacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private String frecuencia;
    private Boolean ignorar;
    private String mensaje;
    private Boolean enviada;
    private TipoNotificacion tipo;
    private Long documentoId;
    private Long usuarioId;
}
