package ni.edu.autotrack_apicore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ni.edu.autotrack_apicore.models.enums.TipoProblema;

@Entity
@Table(name = "registros_problema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroProblema extends Registro{
    
    @Column(name = "afecta_vehiculo", nullable = false)
    private Boolean afectaVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_problema", nullable = false, length = 30)
    private TipoProblema tipoProblema;
}
