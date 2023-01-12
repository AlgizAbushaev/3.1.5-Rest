package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDao {

    List<Role> findAll();

    Role getByName(String roleUser);

    void add(Role role);

    Role getById(int id);


}
