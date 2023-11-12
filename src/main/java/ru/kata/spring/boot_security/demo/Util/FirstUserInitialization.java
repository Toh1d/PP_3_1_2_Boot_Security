package ru.kata.spring.boot_security.demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.RoleService;

import java.util.HashSet;
import java.util.Set;

@Component
public class FirstUserInitialization implements ApplicationRunner {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public FirstUserInitialization(RoleService roleService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);

        Set<Role> adminRoles = new HashSet<>();
        Set<Role> userRoles = new HashSet<>();
        adminRoles.add(roleUser);
        adminRoles.add(roleAdmin);
        userRoles.add(roleUser);

        User adminUser = new User("admin", "adminov", "userAdmin",
                passwordEncoder.encode("100"), adminRoles);
        User user1 = new User("Tohid", "Kurbanov", "tohidka",
                passwordEncoder.encode("100"), userRoles);
        User user2 = new User("Maxim", "Kurakin", "maxonchik",
                passwordEncoder.encode("100"), userRoles);

        userRepository.save(adminUser);
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
