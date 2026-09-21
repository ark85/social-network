package io.github.ark85.study.socialnetwork.model.mapper;

import io.github.ark85.study.socialnetwork.model.DialogMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class DialogMessageRowMapper implements RowMapper<DialogMessage> {
    @Override
    public DialogMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DialogMessage(
                rs.getObject("id", UUID.class),
                rs.getObject("dialog_id", UUID.class),
                rs.getObject("from_user_id", UUID.class),
                rs.getObject("to_user_id", UUID.class),
                rs.getString("text"),
                rs.getObject("creation_date_time", LocalDateTime.class)
        );
    }
}
