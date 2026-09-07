package ru.sinitsyn.app;

import org.hibernate.SessionFactory;
import ru.sinitsyn.dao.*;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.UserService;
import ru.sinitsyn.service.impl.AccountServiceImpl;
import ru.sinitsyn.service.impl.UserServiceImpl;

public class Main {
    public static void main(String[] args){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        UserDao userDao = new HibernateUserDao(sessionFactory);
        UserService userService = new UserServiceImpl(userDao);
        AccountDao accountDao = new HibernateAccountDao(sessionFactory);
        OperationDao operationDao = new HibernateOperationDao(sessionFactory);
        AccountService accountService = new AccountServiceImpl(accountDao, userDao, operationDao);

        User user = userDao.findByLogin("Egor228");


        Account account = accountDao.findByOwnerId(user.getId());
        if (account == null){
            account = accountService.createAccount(user.getId());
        }
        accountService.deposit(account.getId(), new java.math.BigDecimal("500000"));

        System.out.println("User created");

        sessionFactory.close();
    }
}