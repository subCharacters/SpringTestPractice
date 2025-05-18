package com.example.springtestpractice.reader;

import com.example.springtestpractice.dto.Post;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DbToCsvItemReader {

    private final DataSource dataSource;

    public DbToCsvItemReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "dbToCsvReader")
    public JdbcCursorItemReader<Post> reader() {
        return new JdbcCursorItemReaderBuilder<Post>()
                .name("dbToCsvItemReader")
                .dataSource(dataSource)
                .fetchSize(10)
                .sql("SELECT id, title, content, created_at FROM posts")
                .rowMapper(((rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at")
                )))
                // 자동 매핑은 아래와 같이 가능.
                // .beanRowMapper(Post.class)
                // .rowMapper(new BeanPropertyRowMapper<>(Post.class))
                .build();
    }
}
