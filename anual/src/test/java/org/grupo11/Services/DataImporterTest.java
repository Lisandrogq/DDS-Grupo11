package org.grupo11.Services;

import org.grupo11.DataImporter;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Contributor.Individual;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DataImporterTest {
    private ContributionsManager contributionsManager = ContributionsManager.getInstance();
    private ContributorsManager contributorManager = ContributorsManager.getInstance();
    private DataImporter dataImporter;

    @Before
    public void setUp() {// sino se pone re nervioso al correr ambos test de una
        contributionsManager.setContributions(new ArrayList<>());
        contributorManager.setContributors(new ArrayList<>());
    }

    @Test
    public void test_load_contributors() {
        dataImporter = new DataImporter(contributionsManager, contributorManager);
        String fileName = "example.csv";
        dataImporter.loadContributors(fileName);

        // Verify the number of contributors
        assertEquals("Number of contributors should be 2", 17, contributorManager.getContributors().size());

        // Verify the number of contributions
        // Log contributors and contributions

        logContributors();
        logContributions();
        assertEquals("Number of contributions should be 2", 17, contributionsManager.getContributions().size());
    }

    @Test
    public void test_load_contributors_with_duplicate_contributors() {
        dataImporter = new DataImporter(contributionsManager, contributorManager);
        String fileName = "has_duplicates.csv";
        dataImporter.loadContributors(fileName);

        // Verify the number of contributors
        assertEquals("Number of contributors should be 2", 7, contributorManager.getContributors().size());

        // Verify the number of contributions
        // Log contributors and contributions

        logContributors();
        logContributions();
        assertEquals("Number of contributions should be 2", 8, contributionsManager.getContributions().size());
    }

    private void logContributors() {
        System.out.println("Contributors:");
        for (Contributor contributor : contributorManager.getContributors()) {
            if (contributor instanceof Individual) {
                Individual individual = (Individual) contributor;
                System.out.println("   Name: " + individual.getName());
                System.out.println("   Surname: " + individual.getSurname());
                System.out.println("   Document Type: " + individual.getDocumentType());
                System.out.println("   Document Number: " + individual.getDocument());
                List<Contact> contacts = individual.getContacts();
                if (!contacts.isEmpty()) {
                    System.out.println("   Email: " + contacts.get(0));
                }
                System.out.println();
            }
        }
    }

    private void logContributions() {
        System.out.println("Contributions:");
        for (Contribution contribution : contributionsManager.getContributions()) {
            System.out.println("   Contribution Type: " + contribution.getContributionType());
            System.out.println("   Contribution Date: " + contribution.getDate());
            System.out.println();
        }
    }
}
