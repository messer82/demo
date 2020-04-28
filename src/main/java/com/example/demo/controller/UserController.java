package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.exception.AgeValidationException;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mobile_banking_users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user){
        if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() >= 18) {
            return userService.createUser(user);
        } else {
            throw new AgeValidationException("Below 18 years of age!");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(int user_id) {userService.deleteUserById(user_id);}

    @GetMapping("/user_id/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable(name = "user_id") int user_id) {
        return userService.getUserById(user_id);
    }

    @GetMapping("/user_name")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByName(@RequestParam(value = "user_name") String user_name) {
            return userService.getUserByName(user_name);
    }

    @GetMapping("/{user_name}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsersNamedLike(@PathVariable(name = "user_name") String name) {
        return userService.getUsersNamed(name);
    }

    @PatchMapping("/{user_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@PathVariable int user_id, @RequestBody UserPatch userPatch) {
        userPatch.setUserId(user_id);
        return userService.updatePartialUser(userPatch);
    }

    @ExceptionHandler(ValidationException.class)
    public void badRequest(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AgeValidationException.class)
    public void tooYoungForAccountCreation(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendError(HttpStatus.TOO_EARLY.value());
    }
}
