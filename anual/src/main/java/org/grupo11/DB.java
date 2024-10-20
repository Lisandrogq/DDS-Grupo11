package org.grupo11;

import org.grupo11.DTOS.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DB {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Logger.info("Session factory built");
            return new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
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