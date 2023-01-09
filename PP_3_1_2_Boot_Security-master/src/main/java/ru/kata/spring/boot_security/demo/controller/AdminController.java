package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
 private final UserService userService;

    private final RoleService roleService;

    private PasswordEncoder passwordEncoder;
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
@Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

//    @GetMapping(value ="")
//    public String usersALL(Model model) {
//        model.addAttribute("users", userService.allUsers());
//        return "admin";
//    }

    @GetMapping(value = "")
    public String showAllUsers(ModelMap model) {
        List<User> list = userService.listUsers();
        model.addAttribute("users", list);
        return "admin";
    }

    @GetMapping(value = "/addUser")
    public String addNewUser(ModelMap model) {
        User user = new User();
        model.addAttribute("add", true);
        model.addAttribute("user", user);
        return "user-edit";
    }
    @PostMapping(value = "/addUser")
    public String saveNewUser(@ModelAttribute("user") User user,
                              @RequestParam(required = false, name = "ADMIN") String roleAdmin,
                              @RequestParam(required = false, name = "USER") String roleUser) {
        if (roleAdmin != null) {
            user.addRole(roleService.findByName(roleAdmin));
        }
        if (roleUser != null) {
            user.addRole(roleService.findByName(roleUser));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/updateUser/{id}")
    public String saveUser(@ModelAttribute("user") User user, @PathVariable("id") long id,
                           @RequestParam(required = false, name = "ADMIN") String roleAdmin,
                           @RequestParam(required = false, name = "USER") String roleUser) {
        user.setId(id);
        if (roleAdmin != null) {
            user.addRole(roleService.findByName(roleAdmin));
        }
        if (roleUser != null) {
            user.addRole(roleService.findByName(roleUser));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/updateUser/{id}")
    public String updateUser(@PathVariable("id") long id,ModelMap model) {
        User user = userService.getUserById(id);
        Set<Role> roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("add", false);
        model.addAttribute("ruser", roles.contains(roleService.findByName("ROLE_USER")) ? "on" : null);
        model.addAttribute("radmin", roles.contains(roleService.findByName("ROLE_ADMIN")) ? "on" : null);
        return "user-edit";
    }

    @GetMapping(value = "/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeById(id);
        return "redirect:/admin";
    }
}