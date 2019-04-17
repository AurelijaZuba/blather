package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.util.Optional;

public class PostgresUserRepository implements UserRepository {

    PostgresConnector connector;
    public PostgresUserRepository() {
        connector = new PostgresConnector();
    }

    public PostgresUserRepository(PostgresConnector connector) {
        this.connector = connector;
    }

    @Override
    public Optional<User> find(String name) {

        User results = connector.read(name);
        if(results != null)
        {
            return Optional.of(results);
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {

        connector.create(user.name());
    }
}
