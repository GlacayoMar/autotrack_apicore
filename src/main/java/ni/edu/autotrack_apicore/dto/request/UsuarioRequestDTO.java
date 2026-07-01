package ni.edu.autotrack_apicore.dto.request;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nombres;
    private String apellidos;
    private String email;
    private String numeroTel;
    private String username;
    private String pais;
    private String password;
}
