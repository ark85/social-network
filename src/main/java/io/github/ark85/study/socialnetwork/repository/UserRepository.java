package io.github.ark85.study.socialnetwork.repository;

import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.model.mapper.UserRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SqlTypes;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public User getUserById(UUID id) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE id = ?",
                    new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            log.error("User is not found.", ex);
            return null;
        }
    }

    public UUID createUser(User user) {
        return this.jdbcTemplate.queryForObject(
                "INSERT INTO users (first_name, second_name, password_hash, birth_date, biography, city) VALUES " +
                        "(?, ?, ?, ?, ?, ?) returning id",
                UUID.class,
                user.getFirstName(), user.getSecondName(), user.getPasswordHash(),
                user.getBirthDate(), user.getBiography(), user.getCity());
    }

    public List<User> searchUsersByFirstNameAndSecondName(String firstName, String secondName) {
        return this.jdbcTemplate.queryForList(
                "select * from users where first_name LIKE '?%' AND second_name LIKE '?%'",
                User.class, firstName, secondName);
    }

    public void createAll(List<User> users) {
        this.jdbcTemplate.batchUpdate(
                "INSERT INTO users (first_name, second_name, password_hash, birth_date, biography, city) VALUES " +
                        "(?, ?, ?, ?, ?, ?)",
                users,
                users.size(),
                (preparedStatement, user) -> {
                    preparedStatement.setString(1, user.getFirstName());
                    preparedStatement.setString(2, user.getSecondName());
                    preparedStatement.setString(3, user.getPasswordHash());
                    preparedStatement.setObject(4, user.getBirthDate());
                    preparedStatement.setString(5, user.getBiography());
                    preparedStatement.setString(6, user.getCity());
                }
        );
    }
}
