package com.example.springtestpractice.writer;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DbToCsvItemWriter.class)
@TestPropertySource(properties = "job.output.path=test-output")
class DbToCsvItemWriterTest {

    @Autowired
    private FlatFileItemWriter<Post> writer;

    @Test
    void dbToCsvWriter가_파일을_정상적으로_생성한다() throws Exception {
        // given
        Post post = new Post(100L, "파일 쓰기 테스트", "파일 내용 테스트", Timestamp.valueOf("2025-05-20 13:00:00"));

        writer.open(new ExecutionContext());
        // when
        writer.write(new Chunk<>(List.of(post)));

        // then
        File file = new File("test-output/posts.csv");
        assertThat(file.exists()).isTrue();

        List<String> lines = Files.readAllLines(file.toPath());
        assertThat(lines.stream().anyMatch(line -> line.contains("파일 쓰기 테스트"))).isTrue();
    }
}