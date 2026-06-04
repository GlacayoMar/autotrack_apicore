package ni.edu.autotrack_apicore.models.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntidadBase extends Auditoria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
