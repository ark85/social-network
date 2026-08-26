package io.github.ark85.study.socialnetwork.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Slf4j
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;
}
