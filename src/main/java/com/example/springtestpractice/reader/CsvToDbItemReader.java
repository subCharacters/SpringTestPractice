package com.example.springtestpractice.reader;

import com.example.springtestpractice.dto.Post;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CsvToDbItemReader {

    @Value("${job.input.path}")
    private String inputFilePath;

    @Bean(name = "csvToDbReader2")
    public FlatFileItemReader<Post> csvToDbReader2() {
        return new FlatFileItemReaderBuilder<Post>()
                .name("csvToDbItemReader2")
                .resource(new ClassPathResource(inputFilePath + "/posts.csv"))
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<Post>() {{
                    setLineTokenizer(new DelimitedLineTokenizer());
                    setFieldSetMapper(new FieldSetMapper<Post>() {
                        @Override
                        public Post mapFieldSet(FieldSet fieldSet) throws BindException {
                            Post post = new Post();
                            post.setId(fieldSet.readLong(0));
                            post.setTitle(fieldSet.readString(1));
                            post.setContent(fieldSet.readString(2));
                            post.setCreatedAt(Timestamp.valueOf(LocalDateTime.parse(fieldSet.readString(3), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                            return post;
                        }
                    });
                }})
                .build();
    }

    @Bean(name = "csvToDbReader")
    public FlatFileItemReader<Post> reader() {
        return new FlatFileItemReaderBuilder<Post>()
                .name("csvToDbItemReader")
                .resource(new ClassPathResource(inputFilePath + "/posts.csv"))
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("id", "title", "content", "createdAt")
                .fieldSetMapper(new FieldSetMapper<Post>() {
                    @Override
                    public Post mapFieldSet(FieldSet fieldSet) throws BindException {
                        Post post = new Post();
                        post.setId(fieldSet.readLong("id"));
                        post.setTitle(fieldSet.readString("title"));
                        post.setContent(fieldSet.readString("content"));
                        post.setCreatedAt(Timestamp.valueOf(LocalDateTime.parse(fieldSet.readString("createdAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                        return post;
                    }
                })
                .build();
    }
}
