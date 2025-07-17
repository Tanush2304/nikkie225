package com.nichi.nikkie225.utils;

import com.nichi.nikkie225.model1.StocksList;
import com.nichi.nikkie225.model1.TradeList;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static void connectToDatabase(){
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(StocksList.class)
                    .addAnnotatedClass(TradeList.class)
                    .buildSessionFactory();
            System.out.println("running");
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

