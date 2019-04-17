package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class PostgresConnector {

    SessionFactory sessionFactory;

    public PostgresConnector() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public PostgresConnector(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(String name){
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        UserModel userModel = new UserModel();
        userModel.setName(name);

        session.save(userModel);
        session.getTransaction().commit();
        session.close();
    }

    public User read(String name) {
        Session session = sessionFactory.openSession();

        String sql = "FROM UserModel";
        Query query = session.createQuery(sql);
        List result = query.list();
        System.out.println(result);

        for (Object user : result)
        {
            System.out.println(user);
        }

        session.close();

        return null;
    }
}
