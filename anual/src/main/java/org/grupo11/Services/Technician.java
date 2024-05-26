package org.grupo11.Services;

enum TechnicianType {
}

enum AreasOfWork {
    CABA,
    ZonaNorte,
    ZonaSur,
    ZonaOeste,
}

public class Technician {
    String name;
    String surname;
    TechnicianType type;
    String dni;
    String cuil;
    String areasOfWork;
}