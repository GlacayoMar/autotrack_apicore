package ni.edu.autotrack_apicore.services;

import ni.edu.autotrack_apicore.models.Usuario;

public interface UserDetailsService {
    Usuario loadUserByUsername(String username);
}
