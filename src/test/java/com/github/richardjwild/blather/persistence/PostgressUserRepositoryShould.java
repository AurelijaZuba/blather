package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
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
    private SessionFactory session;

    @Before
    public void setUp() throws Exception {
        flyway.migrate();
        PostgresConnector connector = new PostgresConnector(session);
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

    private void setSession(){
        this.session = session;
    }
}
