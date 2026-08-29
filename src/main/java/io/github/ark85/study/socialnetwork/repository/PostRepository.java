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

    public Post getPostById(UUID id) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "SELECT * FROM posts WHERE id = ?",
                    new PostRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            log.error("Post is not found.", ex);
            return null;
        }
    }

    public List<Post> getPosts(int offset, int limit) {
        return this.jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY creation_date_time OFFSET ? LIMIT ?",
                new PostRowMapper(), offset, limit);
    }

    public void createAll(List<String> posts) {
        this.jdbcTemplate.batchUpdate(
                "INSERT INTO posts (text, creation_date_time) VALUES " +
                        "(?, ?)",
                posts,
                posts.size(),
                (preparedStatement, post) -> {
                    preparedStatement.setString(1, post);
                    preparedStatement.setObject(2, LocalDateTime.now());
                }
        );
    }
}
