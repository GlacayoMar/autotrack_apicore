package ni.edu.autotrack_apicore.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registros_problema")
@Getter
@Setter
public class RegistroProblema extends Registro{
}
