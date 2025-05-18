package com.example.springtestpractice.writer;

import com.example.springtestpractice.dto.Post;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class DbToCsvItemWriter {

    @Value("${job.output.path}")
    private String outputFilePath;

    @Bean(name = "dbToCsvWriter")
    public FlatFileItemWriter<Post> writer() {
        return new FlatFileItemWriterBuilder<Post>()
                .name("dbToCsvItemWriter")
                .resource(new FileSystemResource(outputFilePath + "/posts.csv"))
                .lineAggregator(new DelimitedLineAggregator<>() {{
                    setDelimiter(",");
                    setFieldExtractor((FieldExtractor<Post>) item -> new Object[]{
                            item.getId(), item.getTitle(), item.getContent(), item.getCreatedAt()
                    });
                }})
                .build();
    }
}
