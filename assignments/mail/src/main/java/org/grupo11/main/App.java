package org.grupo11.main;

import java.util.ArrayList;

import org.grupo11.services.Client;
import org.grupo11.services.Business.Branch;
import org.grupo11.services.Business.Business;
import org.grupo11.services.Employee.Employee;
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
        Client receiver = new Client("Tomas", "La Casa Del receiver", "Monte castro", 12345);
        Client receiver2 = new Client("Tomas", "La Casa Del receiver2", "Monte castro", 12345);
        float price = 10000;
        Track track1 = new Track(Track.ShippingState.PENDING, new ArrayList<Tramo>());
        Track track2 = new Track(Track.ShippingState.PENDING, new ArrayList<Tramo>());
        Shipping shipping1 = new Shipping(sender, receiver, price, track1);
        Shipping shipping2 = new Shipping(sender, receiver, price, track2); 

        Tramo tramo1A = new Tramo(sender, branch, postman1);
        Tramo tramo1B = new Tramo(branch, receiver, postman2);
        shipping1.getTrack().addTramos(tramo1A);
        shipping1.getTrack().addTramos(tramo1B);

        Tramo tramo2A = new Tramo(sender,receiver2, postman1);
        shipping2.getTrack().addTramos(tramo2A);

        branch.addShipping(shipping1);
        branch.addShipping(shipping2);

        postman1.addShipping(shipping1);
        postman2.addShipping(shipping1);
        postman1.addShipping(shipping2);

        // COMO FUNCIONARIA EL SISTEMA:
        //shipping1
        postman1.updateCurrentShip();
        postman1.startShip();
        postman1.finishCurrentShip();

        //shipping2
        postman1.updateCurrentShip();
        postman1.startShip();
        postman1.finishCurrentShip();
        System.out.println("Shipping1 entregado por: " + shipping1.getLastPostman().getName());

        //shipping1
        postman2.updateCurrentShip();
        postman2.startShip();
        postman2.finishCurrentShip();
        System.out.println("Shipping2 entregado por: " + shipping2.getLastPostman().getName());

        System.out.println("__________________________");
        printBusinessInfo();

    }

    public Business getCorreo() {
        return correo;
    }

    // here we would add function to print out the info

    public void printBusinessInfo() {
        for (Branch branch : this.correo.getAllBranches()) {
            for (Shipping shipping : branch.getShippings()) {
                System.out.println("Shipping: " + shipping.getTrackingCode());
                for (Tramo tramo : shipping.getTrack().getTramos()) {
                    System.out.println("Tramo: ");
                    System.out.println("    Origen: " + tramo.getOrigin().getAddress());
                    System.out.println("    Destino: " + tramo.getDestination().getAddress());
                    System.out.println("    Postman: " + tramo.getPostman().getName());

                }
            }
        }
    }
}
