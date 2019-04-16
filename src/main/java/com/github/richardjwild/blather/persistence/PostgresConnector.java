package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.user.UserModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PostgresConnector {

    public void create(String name){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        UserModel userModel = new UserModel();
        userModel.setName(name);

        session.save(userModel);
        session.getTransaction().commit();
        session.close();
    }
}
