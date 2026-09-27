package io.github.ark85.study.socialnetwork.repository;

import io.github.ark85.study.socialnetwork.model.Post;
import io.github.ark85.study.socialnetwork.model.mapper.PostRowMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Post> getUserPosts(UUID userId, int offset, int limit) {
        return this.jdbcTemplate.query(
                "SELECT post.* FROM friends friend_record JOIN posts post " +
                        "ON post.author_id = friend_record.friend_id WHERE friend_record.user_id = ? " +
                        "ORDER BY creation_date_time DESC OFFSET ? LIMIT ?",
                new PostRowMapper(), userId, offset, limit);
    }

    public List<Post> getUserPosts(UUID userId, int limit) {
        return this.jdbcTemplate.query(
                "SELECT post.* FROM friends friend_record JOIN posts post " +
                        "ON post.author_id = friend_record.friend_id WHERE friend_record.user_id = ? " +
                        "ORDER BY creation_date_time DESC LIMIT ?",
                new PostRowMapper(), userId, limit);
    }

    public UUID createPost(Post post) {
        return this.jdbcTemplate.queryForObject(
                "INSERT INTO posts (author_id, text, creation_date_time) VALUES (?, ?, ?) RETURNING id",
                UUID.class, post.getAuthorId(), post.getText(), post.getCreationDateTime());
    }

    public int updatePost(UUID postId, String postText) {
        return this.jdbcTemplate.update("UPDATE posts SET text = ? WHERE id = ?", postText, postId);
    }

    public int deletePost(UUID id) {
        return this.jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    public void createAll(UUID userId, List<String> posts) {
        this.jdbcTemplate.batchUpdate(
                "INSERT INTO posts ( author_id, text, creation_date_time) VALUES " +
                        "(?, ?, ?)",
                posts,
                posts.size(),
                (preparedStatement, post) -> {
                    preparedStatement.setObject(1, userId);
                    preparedStatement.setString(2, post);
                    preparedStatement.setObject(3, LocalDateTime.now());
                }
        );
    }
}
