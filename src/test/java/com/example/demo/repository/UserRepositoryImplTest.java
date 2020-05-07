package com.example.demo.repository;

import com.example.demo.domain.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
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

        when(userRepository.findById(user2.getUserId())).thenReturn(user2);

        verify(userRepository, times(1)).deleteById(user2.getUserId());

        userRepository.deleteById(user2.getUserId());

        assertThat(jdbcTemplate.update(anyString(), user2.getUserId())).isEqualTo(0);
    }
}
