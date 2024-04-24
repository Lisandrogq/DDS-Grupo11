package org.grupo11.services.Business;

import java.util.ArrayList;
import java.util.List;

import org.grupo11.services.Employee.Employee;
import org.grupo11.services.Shipping.Shipping;
import org.grupo11.services.Shipping.Stop;
import org.grupo11.utils.Crypto;

public class Branch implements Stop{
    private int id;
    private String address;
    private String locality;
    private List<Employee> employees = new ArrayList<Employee>();
    private List<Shipping> shippings = new ArrayList<Shipping>();

    // Constructor
    public Branch(String address, String locality) {
        this.id = Crypto.generateRandomCode(6);
        this.address = address;
        this.locality = locality;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    // shippings
    public void addShipping(Shipping shipping) {
        shippings.add(shipping);
    }

    public List<Shipping> getAllShippings() {
        return shippings;
    }

    public Shipping getShippingByTrackingCode(int trackingCode) {
        return shippings.stream().filter(shipping -> shipping.getTrackingCode() == trackingCode).findFirst()
                .orElse(null);
    }

    // employees
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    // //logistic
    // public void startDeparture(int trackingCode){//esta funcion se utiliza para iniciar o reiniciar(si tiene varias stops) el viaje.
    //     Shipping shipping = getShippingByTrackingCode(trackingCode);
    //     shipping.departure();
    // }
    // public void startArrival(int trackingCode){//esta funcion se utiliza para finalizar un tramo(puede ser el tramo final o no)
    //     Shipping shipping = getShippingByTrackingCode(trackingCode);
    //     shipping.arrive();
    // }

}
