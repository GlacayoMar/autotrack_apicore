package ni.edu.autotrack_apicore.models.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class EntidadBase extends Auditoria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "eliminado", insertable = false, updatable = false)
    private Boolean eliminado = false;
}
