package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DBInit {
    private final RoleService roleService;
    private final UserService userService;

    public DBInit(RoleService roleService, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleService = roleService;
        this.userService = userService;

    }

    @PostConstruct
    private void postConstruct() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        Role managerRole = new Role("ROLE_MANAGER");

        roleService.add(adminRole);
        roleService.add(userRole);
        roleService.add(managerRole);

        Set<Role> roles_admin = new HashSet<>();
        roles_admin.add(roleService.getByName("ROLE_ADMIN"));
        User admin = new User("admin", "admin", "admin@mail.ru", roles_admin);
        userService.addUser(admin);

        Set<Role> roles_user = new HashSet<>();
        roles_user.add(roleService.getByName("ROLE_USER"));
        User user = new User("user", "user", "user@mail.ru", roles_user);
        userService.addUser(user);

        Set<Role> roles_manager = new HashSet<>();
        roles_manager.add(roleService.getByName("ROLE_MANAGER"));
        User manager = new User("manager", "manager", "manager@mail.ru", roles_manager);
        userService.addUser(manager);
    }
}
