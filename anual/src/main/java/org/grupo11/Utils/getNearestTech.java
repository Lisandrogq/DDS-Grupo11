package org.grupo11.Utils;

import java.util.HashMap;
import java.util.List;

import org.checkerframework.checker.units.qual.min;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianManager;

public class GetNearestTech {

        public static HashMap<String,Object> getNearestTechnician(double fridgeLat, double fridgeLon) {
            List<Technician> technicians = TechnicianManager.getInstance().getTechnicians();
            HashMap<String,Object> map = new HashMap<>();
    
            Technician nearestTech = null;
            double minDistance = Double.MAX_VALUE;
    
            for (Technician tech : technicians) {
                String address = tech.getAddress();
                double[] coordinates;
                try {
                    coordinates = LocationHandler.getCoordinates(address);

                    if (coordinates != null && coordinates.length == 2) {
                        double techLat = coordinates[0];
                        double techLon = coordinates[1];
        
                        double distance = calculateDistance(fridgeLat, fridgeLon, techLat, techLon);
        
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestTech = tech;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
            map.put("technician", nearestTech);
            map.put("distance",minDistance);
            return map;
        }
    
        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            final int EARTH_RADIUS = 6371;
    
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
    
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
            return EARTH_RADIUS * c *1000;//return is in meters
        }

}
