package org.grupo11.DTOS;

import org.grupo11.Services.Contributor.LegalEntity.LegalEntityCategory;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntityType;

public class LegalEntityDTO {
    public LegalEntityType type;
    public LegalEntityCategory category;
    public double points;

    public LegalEntityDTO(LegalEntityType type, LegalEntityCategory category, double points) {
        this.type = type;
        this.category = category;
        this.points = points;
    }
}
