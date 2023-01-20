package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;



    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String userALL(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "Tabs_users";
    }

    @GetMapping("/new")
    public String addUser(User user) {
        return "Tabs_users";
    }

    @PostMapping("/new")
    public String add(@RequestParam("userName") String userName,
                      @RequestParam("email") String email,
                      @RequestParam("password") String password,
                      @RequestParam(required = false, name = "ROLE_ADMIN") String roleAdmin,
                      @RequestParam(required = false, name = "ROLE_USER") String roleUser) {

        Set<Role> roles = new HashSet<>();

        if (roleAdmin != null) {
            roles.add(roleService.getByName("ROLE_ADMIN"));
        }
        if (roleUser != null) {
            roles.add(roleService.getByName("ROLE_USER"));
        }
        if (roleAdmin == null && roleUser == null) {
            roles.add(roleService.getByName("ROLE_USER"));
        }

        User user = new User(userName, email, password, roles);
        user.setRoles(roles);

        try {
            userService.addUser(user);
        } catch (Exception ignored) {

        }
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }


    @GetMapping("/edit/{id}")
    public String updateUser(Model model, @PathVariable("id") long id) {
        model.addAttribute(userService.getUserId(id));
        return "Tabs_users";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user,
                           @RequestParam(required = false, name = "ROLE_ADMIN") String roleAdmin,
                           @RequestParam(required = false, name = "ROLE_USER") String roleUser) {

        Set<Role> roles = new HashSet<>();

        if (roleAdmin != null) {
            roles.add(roleService.getByName("ROLE_ADMIN"));
        }
        if (roleUser != null) {
            roles.add(roleService.getByName("ROLE_USER"));
        }
        if (roleAdmin == null && roleUser == null) {
            roles.add(roleService.getByName("ROLE_USER"));
        }

        user.setRoles(roles);

        userService.updateUser(user);

        return "redirect:/admin";
    }
}