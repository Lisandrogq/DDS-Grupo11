package org.grupo11.Services.Contributor.LegalEntity;

import java.util.List;

import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class LegalEntity extends Contributor {
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LegalEntityType type;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private LegalEntityCategory category;

    public LegalEntity() {
    }

    public LegalEntity(String businessName, String address, LegalEntityType type, LegalEntityCategory category,
            List<ContributionType> possibleContributions) {
        super(businessName, address, possibleContributions);
        this.type = type;
        this.category = category;
    }

    public LegalEntityType getType() {
        return this.type;
    }

    public void setType(LegalEntityType type) {
        this.type = type;
    }

    public LegalEntityCategory getCategory() {
        return this.category;
    }

    public void setCategory(LegalEntityCategory category) {
        this.category = category;
    }

}
