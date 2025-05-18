package com.example.springtestpractice.writer;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@ExtendWith(SpringExtension.class)
@Import(CsvToDbItemWriter.class)
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CsvToDbItemWriterTest {

    @Autowired
    private JdbcBatchItemWriter<Post> writer;

    @Autowired
    private DataSource dataSource;

    @Test
    void writer가_DB에_정상적으로_쓰기_처리를_한다() throws Exception {
        // given
        Post post = new Post(100L, "쓰기 테스트", "내용 테스트", Timestamp.valueOf("2025-05-19 10:00:00"));
        List<Post> items = List.of(post);
        Chunk<Post> chunk = new Chunk<Post>(items);

        writer.afterPropertiesSet();

        // when
        writer.write(chunk);

        // then - DB에서 직접 확인
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM posts WHERE id = 100");
             ResultSet rs = ps.executeQuery()) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("title")).isEqualTo("쓰기 테스트");
        }
    }
}