package com.example.demo.repository;

import com.example.demo.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void deleteById(int id);

    List<User> findAll();

    List<User> findUsersNamed(String name);

    User findById(int id);

    User findByName(String name);

    User save(User user);

    User updateUser(int id, String userName, String email);
}
