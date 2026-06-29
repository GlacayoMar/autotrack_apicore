package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;
import ni.edu.autotrack_apicore.models.enums.Estado;
import java.util.Set;

@Data
public class VehiculoRequestDTO {
    private String marca;
    private String modelo;
    private Integer anio;
    private String placa;
    private String vin;
    private Estado estado;
    private Set<String> imagenes;
    private Long usuarioId;
}
