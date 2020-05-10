package com.example.demo.repository;

import com.example.demo.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired(required = true)
    private JdbcTemplate jdbcTemplate;

    @Override
    public void deleteById(int id) {
        String deleteStatement = "DELETE FROM mobile_banking_users WHERE user_id = ?";
        int deletedRows = jdbcTemplate.update(deleteStatement, id);
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT user_id, user_name, email, birth_date FROM mobile_banking_users";
        RowMapper<User> userRowMapper = getUserRowMapper();
        List<User> users = jdbcTemplate.query(sqlQuery, userRowMapper);
        Collections.sort(users, Comparator.comparing(User::getUserId));

        return users;
    }

    @Override
    public User findById(int id) {
        String sqlQuery = "SELECT user_id, user_name, email, birth_date FROM mobile_banking_users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getUserRowMapper(), id);
    }

    @Override
    public User findByName(String name) {
        String sqlQuery = "SELECT user_id, user_name, email, birth_date FROM mobile_banking_users WHERE user_name = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getUserRowMapper(), name);
    }

    @Override
    public User save(User user) {
        String sqlUpdate = "INSERT INTO mobile_banking_users (user_name, email, birth_date) VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlUpdate, user.getUserName(), user.getEmail(), user.getBirthDate());
        return findAll().stream().max(Comparator.comparing(User::getUserId)).get();
    }

    @Override
    public List<User> findUsersNamed(String name) {
        String sqlQuery = "SELECT user_id, user_name, email, birth_date FROM mobile_banking_users WHERE user_name ILIKE ?";
        RowMapper<User> userByNameRowMapper = getUserRowMapper();
        List<User> usersByName = jdbcTemplate.query(sqlQuery, userByNameRowMapper,"%"+name+"%");
        Collections.sort(usersByName, Comparator.comparing(User::getUserId));

        return usersByName;
    }

    @Override
    public User updateUser(int id, String userName, String email) {
        String sqlUpdate = "UPDATE mobile_banking_users SET user_name = ?, email = ? WHERE user_id = ?";

        jdbcTemplate.update(sqlUpdate, userName, email, id);

        return findById(id);
    }

    private RowMapper<User> getUserRowMapper() {
        return ((rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setUserName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setBirthDate(rs.getDate("birth_date").toLocalDate());
            return user;
        });
    }
}
