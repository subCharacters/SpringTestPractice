package com.example.springtestpractice.repository;

import com.example.springtestpractice.dto.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO posts (title, content) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            return ps;
        }, keyHolder);
        Number key = (Number) keyHolder.getKeys().get("id");
        return key != null ? key.longValue() : null;

/*        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        String sql = "INSERT INTO posts (title, content) VALUES (:title, :content)";

        Map<String, Object> params = Map.of(
                "title", post.getTitle(),
                "content", post.getContent()
        );

        namedTemplate.update(sql, params);*/

/*        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "title", post.getTitle(),
                "content", post.getContent()
        );

        Number id = insert.executeAndReturnKey(new MapSqlParameterSource(params));
        return id.longValue();*/
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM posts ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Post(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getTimestamp("created_at"))
        );
        /* 아래와 같이도 가능
        return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY id DESC",
                new BeanPropertyRowMapper<>(Post.class)
        );*/
    }

    public int update(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
        return jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getId());

/*        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        String sql = "UPDATE posts SET title = :title, content = :content WHERE id = :id";

        Map<String, Object> params = Map.of(
                "id", post.getId(),
                "title", post.getTitle(),
                "content", post.getContent()
        );

        return namedTemplate.update(sql, params);*/
    }
}
