package ru.sinitsyn.dao.Hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.User;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Operation.class)

                .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver")
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5433/bank_db")
                .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "bank_user")
                .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, "bank_password")

                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .buildSessionFactory();

    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}