package io.github.ark85.study.socialnetwork.repository;

import io.github.ark85.study.socialnetwork.model.User;
import io.github.ark85.study.socialnetwork.model.mapper.UserRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class FriendRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<UUID> getFriendIds(UUID userId) {
        try {
            return this.jdbcTemplate.queryForList(
                    "SELECT friend_id FROM friends WHERE user_id = ?", UUID.class, userId);
        } catch (EmptyResultDataAccessException ex) {
            log.error("Friends are not found.", ex);
            return null;
        }
    }

    public List<User> getFriends(UUID userId) {
        try {
            return this.jdbcTemplate.query(
                    "SELECT user.* FROM friends friend_record JOIN users user ON user.id = friend_record.friend_id " +
                            "WHERE friend_record.user_id = ?", new UserRowMapper(), userId);
        } catch (EmptyResultDataAccessException ex) {
            log.error("Friends are not found.", ex);
            return null;
        }
    }

    public void setFriend(UUID userId, UUID friendId) {
        this.jdbcTemplate.queryForObject(
                "INSERT INTO friends (user_id, friend_id) VALUES (?, ?) RETURNING id", UUID.class, userId, friendId);
        this.jdbcTemplate.queryForObject(
                "INSERT INTO friends (user_id, friend_id) VALUES (?, ?) RETURNING id", UUID.class, friendId, userId);
    }

    public void deleteFriend(UUID userId, UUID friendId) {
        this.jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
        this.jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", friendId, userId);
    }
}
