package com.example.springtestpractice.repository;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import({PostRepository.class})
@ActiveProfiles("test")
public class PostRepositoryWithBeforAfterTest {

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO posts (title, content) VALUES (?,?)", "사전 등록 제목1", "사전 등록 내용1");
        jdbcTemplate.update("INSERT INTO posts (title, content) VALUES (?,?)", "사전 등록 제목2", "사전 등록 내용2");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("delete from posts");
    }

    @Test
    void 데이터를_검색_할_수_있다() {
        // given
        // when
        List<Post> result = postRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(2);
        // 복수개의 데이터 검증
        // 1.
        assertThat(result).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("사전 등록 제목1", "사전 등록 제목2");

        // 2.
        List<String> titles = result.stream()
                .map(Post::getTitle)
                .collect(Collectors.toList());

        assertThat(titles).contains("사전 등록 제목1", "사전 등록 제목2");

    }
}
