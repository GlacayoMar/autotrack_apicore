package ni.edu.autotrack_apicore.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String nombres;
    private String apellidos;
    private String email;
    private String numeroTel;
    private String username;
    private String pais;
}
