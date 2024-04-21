package org.grupo11.services.Employee;

public class Administrative extends Employee {
    private String office;
    private String department;

    public Administrative(String name, String address, String phoneNumber, String office, String department) {
        super(name, address, phoneNumber);
        this.office = office;
        this.department = department;
    }

    // Getters
    public String getOffice() {
        return office;
    }

    public String getDepartment() {
        return department;
    }
}
