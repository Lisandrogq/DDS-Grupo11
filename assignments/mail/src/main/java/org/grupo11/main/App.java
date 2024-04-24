package org.grupo11.main;

import java.util.ArrayList;

import org.grupo11.services.Client;
import org.grupo11.services.Business.Branch;
import org.grupo11.services.Business.Business;
import org.grupo11.services.Employee.Postman;
import org.grupo11.services.Shipping.Shipping;
import org.grupo11.services.Shipping.Track;
import org.grupo11.services.Shipping.Tramo;

/**
 * Here we would declare the Correo business and add some fns to play
 * around with it.
 */
public class App {
    private Business correo;

    public void Start() {
        this.correo = new Business("Correo argentino", new ArrayList<>());
        Branch branch = new Branch("larrazabal", "Mataderos");
        correo.addBranch(branch);
        Postman postman1 = new Postman("Javier", "Gaona y cuenca", "+54 11-3212-4023");
        Postman postman2 = new Postman("Pepe", "x y y", "+54 11-3212-4023");
        branch.addEmployee(postman1);
        branch.addEmployee(postman2);
        // add a shipping
        Client sender = new Client("Albert", "Juan.b Justo y carrasco", "Monte castro", 12345);
        Client receiver = new Client("Tomas", "Juan.b Justo y carrasco", "Monte castro", 12345);
        float price = 10000;
        Track track = new Track(Track.ShippingState.PENDING, new ArrayList<Tramo>());
        Shipping shipping = new Shipping(sender, receiver, price, track);
        Tramo tramo1 = new Tramo(sender, branch, postman1);
        Tramo tramo2 = new Tramo(branch, receiver, postman2);
        shipping.getTrack().addTramos(tramo1);
        shipping.getTrack().addTramos(tramo2);
        branch.addShipping(shipping);
        postman1.addShipping(shipping);
        postman2.addShipping(shipping);

        // COMO FUNCIONARIA EL SISTEMA:
        postman1.updateCurrentShip();
        postman1.startShip();
        postman1.finishCurrentShip();
        
        postman2.updateCurrentShip();
        postman2.startShip();
        postman2.finishCurrentShip();

    }

    public Business getCorreo() {
        return correo;
    }

    // here we would add function to print out the info

}
