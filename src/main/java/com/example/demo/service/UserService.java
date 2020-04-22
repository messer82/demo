package com.example.demo.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User updatePartialUser(@Valid UserPatch userPatch) {
        User user = userRepository.findById(userPatch.getUserId());
        user.setUserName(userPatch.getUserName());
        user.setEmail(userPatch.getEmail());
        return userRepository.save(user);
    }
}
