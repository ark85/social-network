package io.github.ark85.study.socialnetwork.model.mapper;

import io.github.ark85.study.socialnetwork.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Post(
                rs.getObject("id", UUID.class),
                rs.getString("text"),
                rs.getObject("creation_date_time", LocalDateTime.class)
        );
    }
}
