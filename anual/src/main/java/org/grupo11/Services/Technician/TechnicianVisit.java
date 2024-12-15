package org.grupo11.Services.Technician;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TechnicianVisit {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Technician technician;
    @ElementCollection
    private List<String> pictureUrls;
    private String description;
    private String fridgeName;
    private String fridgeAddress;
    private long visitDate;
    private boolean fixedIt;

    public TechnicianVisit() {
    }

    public TechnicianVisit(Technician technician, List<String> pictureUrls, String description, String fridgeName,
            String fridgeAddress, long visitDate, boolean fixedIt) {
        this.technician = technician;
        this.pictureUrls = pictureUrls;
        this.description = description;
        this.fridgeName = fridgeName;
        this.fridgeAddress = fridgeAddress;
        this.visitDate = visitDate;
        this.fixedIt = fixedIt;
    }

    public String getFridgeAddress() {
        return fridgeAddress;
    }

    public String getFridgeName() {
        return fridgeName;
    }

    public void setFridgeAddress(String fridgeAddress) {
        this.fridgeAddress = fridgeAddress;
    }

    public void setFridgeName(String fridgeName) {
        this.fridgeName = fridgeName;
    }

    public Technician getTechnician() {
        return this.technician;
    }

    public List<String> getPictureUrls() {
        return this.pictureUrls;
    }

    public String getDescription() {
        return this.description;
    }

    public Map<String, Object> toMap() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format for only the date
        String formattedDate = dateFormat.format(new Date(visitDate));
        Map<String, Object> fridgeMap = new HashMap<>();

        fridgeMap.put("emoji", fixedIt ? "🔧" : "🧳");
        fridgeMap.put("type", (fixedIt ? "Fixed the " : "Visit to ") + fridgeName + " fridge ");
        fridgeMap.put("desc",
                "On " + formattedDate + (fixedIt ? " fixed the " : " visited ") + fridgeName + " at " + fridgeAddress);
        return fridgeMap;
    }
}
