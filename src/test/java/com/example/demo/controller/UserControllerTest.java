package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.model.UserPatch;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        verify(userService).getUsers();
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

        verify(userService).createUser(eq(user));
    }

    @Test
    public void test_delete_user() {
        int userId = 5;

        userController.deleteUser(userId);

        verify(userService).deleteUserById(eq(userId));
    }

    @Test
    public void test_get_user_by_id() {
        userController.getUserById(anyInt());

        verify(userService).getUserById(anyInt());
    }

    @Test
    public void test_get_user_by_name() {
        userController.getUserByName(anyString());

        verify(userService).getUserByName(anyString());
    }

    @Test
    public void test_get_users_named_like() {
        userController.getUsersNamedLike(anyString());

        verify(userService).getUsersNamed(anyString());
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

        verify(userService).updatePartialUser(eq(userPatch));
    }
}
