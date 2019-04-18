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
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public PostgresConnector(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(String name){
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        UserModel userModel = new UserModel();
        userModel.setName(name);

        if(isExisting(name)) {
            session.save(userModel);
        }

        session.getTransaction().commit();
        session.close();
    }

    private boolean isExisting(String username) {
        UserModel userModel = this.read(username);
        return userModel == null;
    }

    public UserModel read(String name) {
        UserModel result = null;
        Session session = sessionFactory.openSession();

        Query query = generateFindUserQuery(name, session);
        query.setCacheable(true);
        List foundUsers = query.list();

        if(foundUsers.size() == 1)
            result = (UserModel) foundUsers.get(0);

        session.close();

        return result;
    }

    private Query generateFindUserQuery(String name, Session session) {
        String sql = "FROM UserModel U WHERE U.name = :name";
        return session.createQuery(sql)
                .setParameter("name", name);
    }
}
