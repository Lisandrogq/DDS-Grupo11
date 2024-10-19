package org.grupo11.DTOS;

import org.grupo11.Services.Fridge.Incident.AlertType;

public class AlertDTO {
    public AlertType type;
    public long detectedAt;
    public int fridge_id;

}
