package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistroRequestDTO {
    private LocalDate fechaRegistro;
    private String nota;
    private Long vehiculoId;
}
