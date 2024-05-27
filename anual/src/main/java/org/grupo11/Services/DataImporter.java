package org.grupo11.Services;

import java.text.ParseException;
import java.util.List;
import java.util.function.Consumer;

import org.grupo11.Services.Contact.Email;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.ContributionsManager;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributor.ContributorsManager;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Utils.CSVReader;
import org.grupo11.Utils.DateUtils;
import org.grupo11.enums.DocumentType;

public class DataImporter {
    ContributionsManager contributionsManager;
    ContributorsManager contributorManager;

    public DataImporter(ContributionsManager contributionsManager, ContributorsManager contributorManager) {
        this.contributionsManager = contributionsManager;
        this.contributorManager = contributorManager;
    }

    public void loadContributors(String fileName) {
        // Define the callback as a lambda expression
        Consumer<List<String>> onRead = fields -> {
            try {
                processFields(fields);
            } catch (ParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                // Handle exceptions
                e.printStackTrace();
            }
        };
        CSVReader.read(fileName, onRead);
    }

    private void processFields(List<String> fields) throws ParseException {
        // Parse fields
        DocumentType documentType = DocumentType.valueOf(fields.get(0));
        int document = Integer.parseInt(fields.get(1));
        String name = fields.get(2);
        String surname = fields.get(3);
        String mail = fields.get(5);
        long contributionDate = DateUtils.parseDateString(fields.get(4));
        String contributionType = fields.get(6);
        int quantity = Integer.parseInt(fields.get(7));

        createContribution(documentType, document, name, surname, mail, contributionDate, contributionType, quantity);
    }

    private void createContribution(DocumentType documentType, int document, String name, String surname,
            String mail, long contributionDate, String contributionType, int quantity) {

        // Add contribution if valid
        Individual contributor = contributorManager.getIndividualByDocument(document);
        if (contributor == null) {
            contributor = new Individual(name, surname, null, null, document, documentType);
            contributorManager.add(contributor);
            contributor.addContact(new Email(mail));
        }

        Contribution contribution = null;
        switch (contributionType) {
            case "DINERO":
                contribution = new MoneyDonation(quantity, contributionDate);
                break;
            case "DONACION_VIANDAS":
                contribution = new MealDonation(null, contributionDate);
                break;
            case "REDISTRIBUCION_VIANDAS":
                contribution = new MealDistribution(null, null, quantity, null, null, contributionDate);
                break;
            case "ENTREGA_TARJETAS":
                contribution = new PersonRegistration(null, contributionDate);
                break;
        }
        if (contribution != null)
            contributor.addContribution(contribution);
    }
}
