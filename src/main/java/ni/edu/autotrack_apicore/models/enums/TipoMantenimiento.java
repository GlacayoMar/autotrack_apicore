package ni.edu.autotrack_apicore.models.enums;

public enum TipoMantenimiento {
    CAMBIO_ACEITE_FILTRO,  // El más común (motor)
    SISTEMA_FRENOS,        // Pastillas, discos, líquido de frenos
    LLANTAS_Y_ALINEACION,  // Rotación, balanceo, cambio de neumáticos
    SUSPENSION_DIRECCION,  // Amortiguadores, terminales, esferas
    SISTEMA_ELECTRICO,     // Batería, alternador, luces, fusibles
    SISTEMA_ENFRIAMIENTO,  // Radiador, refrigerante, mangueras
    TRANSMISION_EMBRAGUE,  // Caja de cambios, clutch, líquido de transmisión
    MOTOR_Y_COMBUSTIBLE,   // Bujías, banda de tiempo, filtros de aire/combustible
    AIRE_ACONDICIONADO,    // Carga de gas, filtro de cabina
    REPARACION_CORRECTIVA, // Para fallos inesperados o colisiones (mecánica general)
    INSPECCION_GENERAL,    // Revisión técnica de rutina o pre-viaje
    OTRO
}
