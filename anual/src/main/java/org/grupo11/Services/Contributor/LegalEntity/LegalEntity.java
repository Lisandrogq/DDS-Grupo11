package org.grupo11.Services.Contributor.LegalEntity;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.Services.Credentials;
import org.grupo11.Services.Contributions.ContributionType;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Utils.Crypto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class LegalEntity extends Contributor {
    @Id
    @GeneratedValue
    Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LegalEntityType type;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private LegalEntityCategory category;
    @OneToOne
    private Credentials credentials;
    String orgName;

    public LegalEntity() {
    }

    public LegalEntity(String businessName, String address, LegalEntityType type, LegalEntityCategory category,
            List<ContributionType> possibleContributions) {
        super(businessName, address, possibleContributions);
        this.id = Crypto.genId();
        this.orgName = businessName;
        this.type = type;
        this.category = category;
    }

    public LegalEntity(String businessName, String address, LegalEntityType type, LegalEntityCategory category) {
        super(businessName, address, new ArrayList<>());
        this.id = Crypto.genId();
        this.type = type;
        this.category = category;
    }

    public LegalEntityType getType() {
        return this.type;
    }

    public String getName() {
        return this.orgName;
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

    public Long getId() {
        return id;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
