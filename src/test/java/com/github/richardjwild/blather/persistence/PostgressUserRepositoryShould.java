package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserModel;
import com.github.richardjwild.blather.user.UserRepository;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;

public class PostgressUserRepositoryShould {

    String testDatabaseUrl = "jdbc:postgresql://localhost:5432/test";
    String testUser = "testuser";
    String password = "testPassword123";
    Flyway flyway = Flyway.configure().dataSource(testDatabaseUrl, testUser, password).load();

    private UserRepository userRepository;
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        flyway.migrate();
        configureSessionFactory();
        PostgresConnector connector = new PostgresConnector(sessionFactory);
        userRepository = new PostgresUserRepository(connector);
    }

    @After
    public void tearDown() throws Exception {
        flyway.clean();
    }


    @Test
    public void return_empty_when_user_not_found() {
        Optional<User> result = userRepository.find("will_not_be_found");

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void return_stored_user_when_user_is_found() {
        String userName = "will_be_found";
        User expectedUser = new User(userName);
        userRepository.save(expectedUser);

        Optional<User> actualUser = userRepository.find(userName);

        assertThat(actualUser.isPresent()).isTrue();
        assertThat(actualUser.get()).isSameAs(expectedUser);
    }

    @Test
    public void not_store_duplicate_users_when_the_same_user_saved_twice() {
        String userName = "user_name";
        User user = new User(userName);
        userRepository.save(user);
        User userWithSameName = new User(userName);

        userRepository.save(userWithSameName);

        Optional<User> actualUser = userRepository.find(userName);
        assertThat(actualUser.get()).isSameAs(user);
        assertThat(actualUser.get()).isNotSameAs(userWithSameName);
    }

    private void configureSessionFactory(){
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", testDatabaseUrl);
        configuration.setProperty("hibernate.connection.username", testUser);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.addAnnotatedClass(UserModel.class);
        this.sessionFactory = configuration.buildSessionFactory();
    }
}
