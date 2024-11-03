package org.grupo11;

import org.grupo11.Config.Env;
import org.grupo11.Services.Meal;
import org.grupo11.Services.ActivityRegistry.ActivityRegistry;
import org.grupo11.Services.ActivityRegistry.CardUsage;
import org.grupo11.Services.ActivityRegistry.ContributorRegistry;
import org.grupo11.Services.ActivityRegistry.PINRegistry;
import org.grupo11.Services.Contact.Contact;
import org.grupo11.Services.Contact.EmailContact;
import org.grupo11.Services.Contact.Phone;
import org.grupo11.Services.Contact.WhatsApp;
import org.grupo11.Services.Contributions.Contribution;
import org.grupo11.Services.Contributions.FridgeAdmin;
import org.grupo11.Services.Contributions.MealDistribution;
import org.grupo11.Services.Contributions.MealDonation;
import org.grupo11.Services.Contributions.MoneyDonation;
import org.grupo11.Services.Contributions.PersonRegistration;
import org.grupo11.Services.Contributions.RewardContribution;
import org.grupo11.Services.Contributor.Contributor;
import org.grupo11.Services.Contributor.Individual;
import org.grupo11.Services.Contributor.LegalEntity.LegalEntity;
import org.grupo11.Services.Fridge.Fridge;
import org.grupo11.Services.Fridge.FridgeNotification;
import org.grupo11.Services.Fridge.FridgeOpenLogEntry;
import org.grupo11.Services.Fridge.FridgeSolicitude;
import org.grupo11.Services.Fridge.Subscription;
import org.grupo11.Services.Fridge.Incident.Alert;
import org.grupo11.Services.Fridge.Incident.Failure;
import org.grupo11.Services.Fridge.Incident.Incident;
import org.grupo11.Services.PersonInNeed.PersonInNeed;
import org.grupo11.Services.Reporter.FailureReportRow;
import org.grupo11.Services.Reporter.MealPerContributorReportRow;
import org.grupo11.Services.Reporter.MealsPerFridgeReportRow;
import org.grupo11.Services.Reporter.Report;
import org.grupo11.Services.Rewards.Reward;
import org.grupo11.Services.Technician.Technician;
import org.grupo11.Services.Technician.TechnicianVisit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DB {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .setProperty("hibernate.connection.url", Env.getDBUrl())
                    .setProperty("hibernate.connection.username", Env.getDBUser())
                    .setProperty("hibernate.connection.password", Env.getDBPassword())
                    .configure()
                    .addAnnotatedClass(FailureReportRow.class)
                    .addAnnotatedClass(MealPerContributorReportRow.class)
                    .addAnnotatedClass(MealsPerFridgeReportRow.class)
                    .addAnnotatedClass(Report.class)
                    .addAnnotatedClass(PersonInNeed.class)
                    .addAnnotatedClass(CardUsage.class)
                    // contributions
                    .addAnnotatedClass(Contribution.class)
                    .addAnnotatedClass(Reward.class)
                    .addAnnotatedClass(MoneyDonation.class)
                    .addAnnotatedClass(PersonRegistration.class)
                    .addAnnotatedClass(MealDistribution.class)
                    .addAnnotatedClass(RewardContribution.class)
                    .addAnnotatedClass(FridgeAdmin.class)
                    .addAnnotatedClass(MealDonation.class)
                    // registry
                    .addAnnotatedClass(ContributorRegistry.class)
                    .addAnnotatedClass(ActivityRegistry.class)
                    .addAnnotatedClass(PINRegistry.class)
                    // technician
                    .addAnnotatedClass(Technician.class)
                    .addAnnotatedClass(TechnicianVisit.class)
                    // fridge
                    .addAnnotatedClass(Alert.class)
                    .addAnnotatedClass(Incident.class)
                    .addAnnotatedClass(Failure.class)
                    .addAnnotatedClass(Subscription.class)
                    .addAnnotatedClass(Meal.class)
                    .addAnnotatedClass(FridgeNotification.class)
                    .addAnnotatedClass(FridgeOpenLogEntry.class)
                    .addAnnotatedClass(FridgeSolicitude.class)
                    .addAnnotatedClass(Fridge.class)
                    // contacts
                    .addAnnotatedClass(Contact.class)
                    .addAnnotatedClass(WhatsApp.class)
                    .addAnnotatedClass(EmailContact.class)
                    .addAnnotatedClass(Phone.class)
                    // contributors
                    .addAnnotatedClass(Contributor.class)
                    .addAnnotatedClass(Individual.class)
                    .addAnnotatedClass(LegalEntity.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            Logger.error("Could not create session factory", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void create(Object object) {
        executeInTransaction(session -> session.persist(object));
    }

    public static void update(Object object) {
        executeInTransaction(session -> session.merge(object));
    }

    public static void delete(Object object) {
        executeInTransaction(session -> session.remove(object));
    }

    public static <T> T find(Class<T> clazz, Object id) {
        try (Session session = getSessionFactory().openSession()) {
            return session.find(clazz, id);
        } catch (Throwable e) {
            throw e;
        }
    }

    private static void executeInTransaction(TransactionOperation operation) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            operation.execute(session);
            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @FunctionalInterface
    interface TransactionOperation {
        void execute(Session session);
    }
}
