package org.grupo11.services.Business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Business {
    public String name;
    private List<Branch> branches;

    public Business(String name, Optional<List<Branch>> branches) {
        this.name = name;
        this.branches = branches.orElse(new ArrayList<Branch>());
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public void rmvBranch(Branch branch) {
        branches.remove(branch);
    }

    public Branch getBrachById(int id) {
        return branches.stream().filter(branch -> branch.getId() == id).findFirst().orElse(null);
    }

    public List<Branch> getAllBranches() {
        return branches;
    }
}
