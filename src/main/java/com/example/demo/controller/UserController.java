package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mobile_banking_users")
public class UserController {

    private final UserService userService;

//    @GetMapping("/test")
//    public String test() {
//        return "testing";
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user){
        return userService.createUser(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(int user_id) {userService.deleteUserById(user_id);}

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable(name = "id") int user_id) {
        return userService.getUserById(user_id);
    }
}
