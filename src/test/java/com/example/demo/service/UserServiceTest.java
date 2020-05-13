package com.example.demo.service;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.exception.AgeValidationException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceTest {

    private User user1 = null;
    private User user2 = null;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepositoryImpl userRepository;

    List<User> users = new ArrayList<>();

    @Before
    public void setup() {
        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();
        user2 = User.
                builder().
                userId(2).
                userName("Jane Smith").
                email("jane.smith@gmail.com").
                birthDate(LocalDate.parse("2001-01-01")).
                build();
        users.add(user1);
        users.add(user2);
    }

    @Test
    public void test_create_user() {

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        Mockito.when(userRepository.save(ArgumentMatchers.any())).
                thenReturn(user1).
                thenThrow(AgeValidationException.class);

        User result = userService.createUser(user1);

        Assertions.assertThat(result).isEqualToComparingFieldByField(user1);

        Mockito.verify(userRepository).save(Mockito.eq(user1));
    }

    @Test
    public void test_delete_user_by_id() {

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        Mockito.when(userRepository.findById(user1.getUserId())).
                thenReturn(user1).
                thenThrow(UserNotFoundException.class);

        userService.deleteUserById(user1.getUserId());

        Mockito.verify(userRepository).deleteById(Mockito.eq(user1.getUserId()));
    }

    @Test
    public void test_get_users() {

        Mockito.when(userRepository.findAll()).
                thenReturn(users).
                thenThrow(UserNotFoundException.class);

        List<User> result = userService.getUsers();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(user1);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(user2);

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void test_get_user_by_id() {

        int userId = 5;

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        Mockito.when(userRepository.findById(ArgumentMatchers.anyInt())).
                thenReturn(user1).
                thenThrow(UserNotFoundException.class);

        User result = userService.getUserById(userId);

        Assertions.assertThat(result).isEqualToComparingFieldByField(user1);

        Mockito.verify(userRepository).findById(Mockito.eq(userId));
    }

    @Test
    public void test_get_user_by_name() {

        String name = "John";

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        Mockito.when(userRepository.findByName(ArgumentMatchers.anyString())).
                thenReturn(user1).
                thenThrow(UserNotFoundException.class);

        User result = userService.getUserByName(name);

        Assertions.assertThat(result).isEqualToComparingFieldByField(user1);

        Mockito.verify(userRepository).findByName(Mockito.eq(name));
    }

    @Test
    public void test_get_users_named() {

        String name = "Jack";

        Mockito.when(userRepository.findUsersNamed(ArgumentMatchers.anyString())).
                thenReturn(users).
                thenThrow(UserNotFoundException.class);

        List<User> result = userService.getUsersNamed(name);

        Assertions.assertThat(result.size()).isEqualTo(users.size());

        Mockito.verify(userRepository).findUsersNamed(Mockito.eq(name));
    }

    @Test
    public void test_update_partial_user() {

        UserPatch userPatch = UserPatch.builder().userId(1).userName("John").email("john@gmail.com").build();

        userService.updatePartialUser(userPatch);

        Mockito.verify(userRepository).updateUser(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
    }
}
