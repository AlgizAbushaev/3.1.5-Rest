package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class IndexController {
    private final UserService userService;


    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String indexPage() {
        return "Hello!";
    }

}
