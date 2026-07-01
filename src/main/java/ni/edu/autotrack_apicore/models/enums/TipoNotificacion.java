package ni.edu.autotrack_apicore.models.enums;

public enum TipoNotificacion {
    VENCIMIENTO_DOCUMENTO,  // Alerta de que la licencia o seguro están por vencer
    MANTENIMIENTO_PROXIMO,  // Alerta basada en kilometraje/fecha para cambio de aceite, etc
    MULTA_REGISTRADA,       // 2 mas y ya vali
    RECORDATORIO_PAGO,      // Para cuotas de seguro, impuestos de rodamiento o lo que sea
    ALERTA_SISTEMA          // alertas generales al usuario como cambio de contrasena o actualizaciones
}
