package com.example.demo.repository;

import com.example.demo.domain.entity.User;

import java.util.List;

public interface UserRepository {
    void deleteById(int id);

    List<User> findAll();

    User findById(int id);

    User findByName(String name);

    User save(User user);
}
