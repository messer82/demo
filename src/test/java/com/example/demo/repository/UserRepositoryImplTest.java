package com.example.demo.repository;

import com.example.demo.domain.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class UserRepositoryImplTest {

    private User user1 = null;
    private User user2 = null;
    @InjectMocks
    private UserRepositoryImpl userRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        user1 = User.builder().userId(1).userName("John Doe").email("john.doe@gmail.com").birthDate(LocalDate.parse("2000-01-01")).build();
        user2 = User.builder().userId(2).userName("Jane Smith").email("jane.smith@gmail.com").birthDate(LocalDate.parse("2001-01-01")).build();
    }

    @Test
    public void test_delete_by_id() {

        int userId = 5;

        userRepository.deleteById(userId);

        verify(jdbcTemplate).update(anyString(), eq(userId));
    }

    @Test
    public void test_find_all() {
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(users);

        List<User> result = userRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(user1);
        assertThat(result.get(1)).isEqualTo(user2);
    }

    @Test
    public void test_find_by_id() {

        int userId = 7;

        userRepository.findById(userId);

        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(userId));
    }

    @Test
    public void test_find_by_name() {

        String userName = "George";

        userRepository.findByName(userName);

        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(userName));
    }
}
