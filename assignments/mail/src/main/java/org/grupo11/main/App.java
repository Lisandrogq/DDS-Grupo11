package org.grupo11.main;

import java.util.ArrayList;

import org.grupo11.services.Client;
import org.grupo11.services.Business.Branch;
import org.grupo11.services.Business.Business;
import org.grupo11.services.Employee.Postman;
import org.grupo11.services.Shipping.Shipping;
import org.grupo11.services.Shipping.Stop;
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
        Postman postman = new Postman("Javier", "Gaona y cuenca", "+54 11-3212-4023");
        branch.addEmployee(postman);

        // add a shipping
        Client sender = new Client("Albert", "Juan.b Justo y carrasco", "Monte castro", 12345);
        Client receiver = new Client("Tomas", "Juan.b Justo y carrasco", "Monte castro", 12345);
        float price = 10000;
        Track track = new Track(Track.ShippingState.PENDING,  new ArrayList<Tramo>(), postman);
        Shipping shipping = new Shipping(sender, receiver, price, track);
        branch.addShipping(shipping);
    }

    public Business getCorreo() {
        return correo;
    }

    // here we would add function to print out the info
}
