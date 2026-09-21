package io.github.ark85.study.socialnetwork.repository;

import io.github.ark85.study.socialnetwork.model.DialogMessage;
import io.github.ark85.study.socialnetwork.model.mapper.DialogMessageRowMapper;
import io.github.ark85.study.socialnetwork.model.mapper.PostRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class DialogRepository {

    private final JdbcTemplate jdbcTemplate;

    public DialogRepository(
            @Qualifier("citusJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID createDialogMessage(DialogMessage dialogMessage) {
        return this.jdbcTemplate.queryForObject(
                "INSERT INTO dialog_messages (dialog_id, from_user_id, to_user_id, text, creation_date_time) " +
                        "VALUES (?, ?, ?, ?, ?) returning id",
                UUID.class,
                dialogMessage.getDialogId(), dialogMessage.getFromUserId(), dialogMessage.getToUserId(),
                dialogMessage.getText(), dialogMessage.getCreationDateTime());
    }

    public List<DialogMessage> getDialogMessages(UUID dialogId) {
        return this.jdbcTemplate.query(
                "SELECT * FROM dialog_messages WHERE dialog_id = ? ORDER BY creation_date_time DESC",
                new DialogMessageRowMapper(), dialogId);
    }
}
