package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.Estado;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class VehiculoResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
    private String marca;
    private String modelo;
    private Integer anio;
    private String placa;
    private String vin;
    private Estado estado;
    private Set<String> imagenes;
    private Long usuarioId;
}
