package ni.edu.autotrack_apicore.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
