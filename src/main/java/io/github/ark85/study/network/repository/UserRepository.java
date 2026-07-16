package io.github.ark85.study.network.repository;

import io.github.ark85.study.network.model.User;
import io.github.ark85.study.network.model.mapper.UserRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public User getUserById(UUID id) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select * from users where id = ?",
                    new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            log.error("User is not found.", ex);
            return null;
        }
    }

    public UUID createUser(User user) {
        return this.jdbcTemplate.queryForObject(
                "insert into users (first_name, second_name, password_hash, birth_date, biography, city) values " +
                        "(?, ?, ?, ?, ?, ?) returning id",
                UUID.class,
                user.getFirstName(), user.getSecondName(), user.getPasswordHash(),
                user.getBirthDate(), user.getBiography(), user.getCity());
    }
}
