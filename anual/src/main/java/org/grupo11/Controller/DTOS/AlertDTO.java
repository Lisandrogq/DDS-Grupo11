package org.grupo11.Controller.DTOS;


import org.grupo11.Services.Fridge.Incident.AlertType;

public class AlertDTO {
    public AlertType type;
    public long detectedAt;
    public int fridge_id;

    public AlertDTO(int fridge_id,AlertType type, long detectedAt) {
      this.type = type;
      this.detectedAt= detectedAt;
      this.fridge_id = fridge_id;
    }
}
