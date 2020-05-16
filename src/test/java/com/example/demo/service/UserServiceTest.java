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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

//    @Test
//    public void given_exception_when_create_user_then_age_validation_exception_is_thrown() {
//        user1 = User.
//                builder().
//                userId(1).
//                userName("John Doe").
//                email("john.doe@gmail.com").
//                birthDate(LocalDate.parse("2005-01-01")).
//                build();
//
////        when(userRepository.save(any())).thenThrow(IllegalArgumentException.class);
//        when(userRepository.save(any())).thenThrow(AgeValidationException.class);
//
//        Throwable throwable = Assertions.catchThrowable(() -> userService.createUser(user1));
//
//        assertThat(throwable).isInstanceOf(AgeValidationException.class);
//        assertThat(throwable.getMessage()).isEqualTo("Below 18 years of age!");
//    }

    @Test
    public void test_create_user() {

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        when(userRepository.save(any())).
                thenReturn(user1).
                thenThrow(AgeValidationException.class);

        User result = userService.createUser(user1);

        assertThat(result).isEqualToComparingFieldByField(user1);

        verify(userRepository).save(eq(user1));
    }

//    @Test
//    public void given_exception_when_delete_user_by_id_then_user_not_found_exception_is_thrown() {
//        int userId = 5;
//    }

    @Test
    public void test_delete_user_by_id() {

        user1 = User.
                builder().
                userId(1).
                userName("John Doe").
                email("john.doe@gmail.com").
                birthDate(LocalDate.parse("2000-01-01")).
                build();

        when(userRepository.findById(user1.getUserId())).
                thenReturn(user1).
                thenThrow(UserNotFoundException.class);

        userService.deleteUserById(user1.getUserId());

        verify(userRepository).deleteById(eq(user1.getUserId()));
    }

    @Test
    public void given_exception_when_get_users_then_user_not_found_exception_is_thrown() {
        List<User> result = userService.getUsers();

        when(userRepository.findAll()).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> userService.getUsers());

        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No users available!");
    }

    @Test
    public void test_get_users() {

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(user1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(user2);

        verify(userRepository).findAll();
    }

    @Test
    public void given_exception_when_get_user_by_id_then_user_not_found_exception_is_thrown() {
        int userId = 5;

        when(userRepository.findById(anyInt())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> userService.getUserById(userId));

        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No user found for this Id!");
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

        when(userRepository.findById(anyInt())).thenReturn(user1);

        User result = userService.getUserById(userId);

        assertThat(result).isEqualToComparingFieldByField(user1);

        verify(userRepository).findById(eq(userId));
    }

    @Test
    public void given_exception_when_get_user_by_name_then_user_not_found_exception_is_thrown() {
        String userName = "Ion";

        when(userRepository.findByName(anyString())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> userService.getUserByName(userName));

        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No user found for this user name!");
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

        when(userRepository.findByName(anyString())).thenReturn(user1);

        User result = userService.getUserByName(name);

        assertThat(result).isEqualToComparingFieldByField(user1);

        Mockito.verify(userRepository).findByName(Mockito.eq(name));
    }

    @Test
    public void given_exception_when_get_users_named_then_user_not_found_exception_is_thrown() {
        String userName = "George";

        when(userRepository.findUsersNamed(anyString())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> userService.getUsersNamed(userName));

        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No users found with relevance to this name!");
    }

    @Test
    public void test_get_users_named() {

        String name = "Jack";

        when(userRepository.findUsersNamed(anyString())).thenReturn(users);

        List<User> result = userService.getUsersNamed(name);

        assertThat(result.size()).isEqualTo(users.size());

        verify(userRepository).findUsersNamed(eq(name));
    }

    @Test
    public void given_exception_when_update_partial_user_then_user_not_found_exception_is_thrown() {
        UserPatch userPatch = UserPatch.builder().userId(1).userName("John Doe").email("john.doe@gmail.com").build();

        when(userRepository.updateUser(anyInt(), anyString(), anyString())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> userService.updatePartialUser(userPatch));

        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No valid user details for patch!");
    }

    @Test
    public void test_update_partial_user() {

        UserPatch userPatch = UserPatch.builder().userId(1).userName("John").email("john@gmail.com").build();

        userService.updatePartialUser(userPatch);

        verify(userRepository).updateUser(anyInt(), anyString(), anyString());
    }
}
