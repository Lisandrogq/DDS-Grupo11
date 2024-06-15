package org.grupo11.Services;

import java.text.ParseException;
import java.util.List;
import java.util.function.Consumer;

import org.grupo11.Enums.DocumentType;
import org.grupo11.Services.Contact.EmailContact;
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

enum ContributionTypeField {
    DINERO,
    DONACION_VIANDAS,
    REDISTRIBUCION_VIANDAS,
    ENTREGA_TARJETAS,
}

public class DataImporter {
    ContributionsManager contributionsManager;
    ContributorsManager contributorManager;

    public DataImporter(ContributionsManager contributionsManager, ContributorsManager contributorManager) {
        this.contributionsManager = contributionsManager;
        this.contributorManager = contributorManager;
    }

    public void loadContributors(String fileName) {
        Consumer<List<String>> onRead = fields -> {
            try {
                processFields(fields);
            } catch (ParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        };
        CSVReader.read(fileName, onRead);
    }

    /**
     * process each field of the csv, mapping them to the expected value type. In
     * case one does not match, then we catch the exception and simply return.
     * So, this would be doing the actual fields data type fields.
     */
    private void processFields(List<String> fields) throws ParseException {
        try {
            DocumentType documentType = DocumentType.valueOf(fields.get(0));
            int document = Integer.parseInt(fields.get(1));
            String name = fields.get(2);
            String surname = fields.get(3);
            String mail = fields.get(4);
            long contributionDate = DateUtils.parseDateString(fields.get(5));
            ContributionTypeField contributionType = ContributionTypeField.valueOf(fields.get(6));
            int quantity = Integer.parseInt(fields.get(7));
          
            createContribution(documentType, document, name, surname, mail, contributionDate, contributionType,
                    quantity);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void createContribution(DocumentType documentType, int document, String name, String surname,
            String mail, long contributionDate, ContributionTypeField contributionType, int quantity) {

        Individual contributor = contributorManager.getIndividualByDocument(document);

        // Add contributor if it does not exists
        if (contributor == null) {
            contributor = new Individual(name, surname, null, null, document, documentType);
            contributorManager.add(contributor);
            contributor.addContact(new EmailContact(mail));
            contributor.getContacts().get(0).SendNotification("new account created", "we've created a new account for you");
        }

        Contribution contribution = null;

        switch (contributionType) {
            case DINERO:
                contribution = new MoneyDonation(quantity, contributionDate);
                break;
            case DONACION_VIANDAS:
                contribution = new MealDonation(null, contributionDate);
                break;
            case REDISTRIBUCION_VIANDAS:
                contribution = new MealDistribution(null, null, quantity, null, null, contributionDate);
                break;
            case ENTREGA_TARJETAS:
                contribution = new PersonRegistration(null, contributionDate);
                break;
        }

        if (contribution != null) {
            if(contribution.contribute(contributor))
                contributionsManager.add(contribution);
        }
    }
}
