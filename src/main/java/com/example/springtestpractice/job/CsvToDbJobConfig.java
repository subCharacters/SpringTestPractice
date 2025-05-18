package com.example.springtestpractice.job;

import com.example.springtestpractice.dto.Post;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CsvToDbJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final int chunkSize = 10;

    public CsvToDbJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job csvToDbJob(Step csvToDbStep) {
        return new JobBuilder("csvToDbJob", jobRepository)
                .start(csvToDbStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step csvToDbStep(@Qualifier("csvToDbReader") ItemReader<Post> csvToDbItemReader,
                            @Qualifier("csvToDbWriter") ItemWriter<Post> csvToDbItemWriter) {
        return new StepBuilder("csvToDbStep", jobRepository)
                .<Post, Post>chunk(chunkSize, transactionManager)
                .reader(csvToDbItemReader)
                .writer(csvToDbItemWriter)
                .build();
    }
}
