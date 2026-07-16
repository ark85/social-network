package io.github.ark85.study.socialnetwork.model.mapper;

import io.github.ark85.study.socialnetwork.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getObject("id", UUID.class),
                rs.getString("first_name"),
                rs.getString("second_name"),
                rs.getString("password_hash"),
                rs.getObject("birth_date", LocalDate.class),
                rs.getString("biography"),
                rs.getString("city")
        );
    }
}
