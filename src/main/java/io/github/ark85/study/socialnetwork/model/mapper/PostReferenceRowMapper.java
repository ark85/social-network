package io.github.ark85.study.socialnetwork.model.mapper;

import io.github.ark85.study.socialnetwork.model.PostReference;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostReferenceRowMapper implements RowMapper<PostReference> {
    @Override
    public PostReference mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PostReference(
                rs.getObject("id", UUID.class),
                rs.getObject("author_id", UUID.class)
        );
    }
}
