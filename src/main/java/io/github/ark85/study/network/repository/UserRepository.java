package io.github.ark85.study.network.repository;

import io.github.ark85.study.network.model.User;
import io.github.ark85.study.network.model.mapper.UserRowMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public User getUserById(UUID id) {
        return this.jdbcTemplate.queryForObject(
                "select * from users where id = ?",
                new UserRowMapper(), id);
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
