package io.github.ark85.study.socialnetwork.model.mapper;

import io.github.ark85.study.socialnetwork.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
