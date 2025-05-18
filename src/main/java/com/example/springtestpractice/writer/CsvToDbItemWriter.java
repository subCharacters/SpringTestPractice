package com.example.springtestpractice.writer;

import com.example.springtestpractice.dto.Post;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CsvToDbItemWriter {

    private final DataSource dataSource;

    public CsvToDbItemWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "csvToDbWriter")
    public JdbcBatchItemWriter<Post> writer() {
        return new JdbcBatchItemWriterBuilder<Post>()
                .dataSource(dataSource)
                .sql("INSERT INTO posts (id, title, content, created_at) VALUES (?, ?, ?, ?)")
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setString(2, item.getTitle());
                    ps.setString(3, item.getContent());
                    ps.setTimestamp(4, item.getCreatedAt());
                })
                .build();
    }
}
