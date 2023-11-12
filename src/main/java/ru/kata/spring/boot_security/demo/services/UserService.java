package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    User findUserByUsername(String username);

    List<User> getAllUsers();

    User getById(long id);

    boolean add(User user);

    void update(User user, long id); //TODO: тут id мб добавить как в гите??

    void delete(long id);
}
