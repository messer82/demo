package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)

public class UserControllerTest {

    private User user = null;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setup() {
        user = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();
    }

    @Test
    public void test_get_users() {
        userController.getUsers();

        Mockito.verify(userService).getUsers();
    }

    @Test
    public void test_create_user() {

        user = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        userController.createUser(user);

        Mockito.verify(userService).createUser(Mockito.eq(user));
    }

    @Test
    public void test_delete_user() {
        int userId = 5;

        userController.deleteUser(userId);

        Mockito.verify(userService).deleteUserById(Mockito.eq(userId));
    }

    @Test
    public void test_get_user_by_id() {
        userController.getUserById(Mockito.anyInt());

        Mockito.verify(userService).getUserById(Mockito.anyInt());
    }

    @Test
    public void test_get_user_by_name() {
        userController.getUserByName(Mockito.anyString());

        Mockito.verify(userService).getUserByName(Mockito.anyString());
    }

    @Test
    public void test_get_users_named_like() {
        userController.getUsersNamedLike(Mockito.anyString());

        Mockito.verify(userService).getUsersNamed(Mockito.anyString());
    }

    @Test
    public void test_update_user() {
        user = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        UserPatch userPatch = UserPatch.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                build();

        userController.updateUser(user.getUserId(), userPatch);

        Mockito.verify(userService).updatePartialUser(Mockito.eq(userPatch));
    }
}
