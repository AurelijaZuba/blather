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

    public UserModel read(String name) {
        Session session = sessionFactory.openSession();

        String sql = "FROM UserModel";
        Query query = session.createQuery(sql);
        List foundUsers = query.list();

        UserModel result = null;

        if(foundUsers.size() == 1)
            result = (UserModel) foundUsers.get(0);

        session.close();

        return result;
    }
}
