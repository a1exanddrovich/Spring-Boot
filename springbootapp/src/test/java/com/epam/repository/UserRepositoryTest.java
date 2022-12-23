package com.epam.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.epam.model.Role;
import com.epam.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        User foundUser =
                new User("TestUser", "test@tes3t.tu", "$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG",
                        "Test",
                        "TestLast", Arrays.asList(new Role()));
        foundUser.setId(1L);
        repository.saveAll(Arrays.asList(foundUser,
                new User("TestUser1", "test@tes3t.tu", "$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG",
                        "Test",
                        "TestLast", Arrays.asList(new Role("ROLE_USER"))),
                new User("TestUser2", "test@tes3t.tu", "$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG",
                        "Test",
                        "TestLast", Arrays.asList(new Role("ROLE_USER")))));
    }

    @Test
    void shouldFindAllUsers() {
        List<User> all = repository.findAll();

        assertThat(all.size(), is(3));
    }

    @Test
    void shouldFindUserWhenExistingIdPassed() {
        Optional<User> user = repository.findById(1L);

        User foundUser =
                new User("TestUser", "test@tes3t.tu", "$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG",
                        "Test",
                        "TestLast", Arrays.asList(new Role()));
        foundUser.setId(1L);

        assertThat(user, is(Optional.of(foundUser)));
    }

    @Test
    void shouldDeleteUserById() {
        repository.deleteById(1L);

        Optional<User> actual = repository.findById(1L);
        assertThat(actual, is(Optional.empty()));
    }

    @Test
    void shouldCreateUser() {
        User newUser =
                new User("TestUser", "test@tes3t.tu", "$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG",
                        "Test",
                        "TestLast", Arrays.asList(new Role()));

        User save = repository.save(newUser);

        assertThat(newUser, is(save));
    }

}
