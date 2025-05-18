package com.example.springtestpractice.reader;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(DbToCsvItemReader.class)
// @ContextConfiguration를 해버리면 @DataJdbcTest에서 필요한 base package정보를 로드 못함.
// 필요한 클래스만 특정해서 로드 해버리기때문.
class DbToCsvItemReaderTest {

    @Autowired
    private JdbcCursorItemReader<Post> dbToCsvItemReader;
    // @Autowired
    // private DataSource dataSource;

   /* @Test
    void dbToCsvItemReader가_정상적으로_작동한다() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("INSERT INTO posts (title, content) VALUES ('제목', '내용')").execute();
        }

        dbToCsvItemReader.open(new ExecutionContext());
        Post post = dbToCsvItemReader.read();
        dbToCsvItemReader.close();

        assertThat("제목").isEqualTo(post.getTitle());
        assertThat("내용").isEqualTo(post.getContent());

        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("DELETE FROM posts").execute();
        }
    }*/

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/insert_posts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void dbToCsvItemReader가_sql로_초기화된_데이터를_정상적으로_작동한다() throws Exception {
        dbToCsvItemReader.open(new ExecutionContext());
        Post post1 = dbToCsvItemReader.read();
        Post post2 = dbToCsvItemReader.read();
        dbToCsvItemReader.close();

        assertThat("사전 등록 제목1").isEqualTo(post1.getTitle());
        assertThat("사전 등록 제목2").isEqualTo(post2.getTitle());
        assertThat("사전 등록 내용1").isEqualTo(post1.getContent());
        assertThat("사전 등록 내용2").isEqualTo(post2.getContent());
    }
}