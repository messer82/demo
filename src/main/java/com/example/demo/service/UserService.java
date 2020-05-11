package com.example.demo.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.exception.AgeValidationException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() >= 18) {
            return userRepository.save(user);
        } else {
            throw new AgeValidationException("Below 18 years of age!");
        }
    }

    public void deleteUserById(int id) {
        if (getUserById(id).getUserId() > 0) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException("No users available!");
        }
    }

    public User getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException("No user found for this Id!");
        }
    }

    public User getUserByName(String name) {
        try {
            return userRepository.findByName(name);
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException("No user found for this user name!");
        }
    }

    public List<User> getUsersNamed(String name) {
        try {
            return userRepository.findUsersNamed(name);
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException("No users found with relevance to this name!");
        }
    }

    public User updatePartialUser(@Valid UserPatch userPatch) {
        try {
            User user = userRepository.findById(userPatch.getUserId());
            return userRepository.updateUser(userPatch.getUserId(), userPatch.getUserName(), userPatch.getEmail());
        } catch (EmptyResultDataAccessException exception) {
            throw new UserNotFoundException("No valid user details for patch!");
        }
    }
}
