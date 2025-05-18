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
public class DbToCsvJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final int chunkSize = 10;

    public DbToCsvJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job dbToCsvJob(Step dbToCsvStep) {
        return new JobBuilder("dbToCsvJob", jobRepository)
                .start(dbToCsvStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step dbToCsvStep(@Qualifier("dbToCsvReader") ItemReader<Post> dbToCsvItemReader,
                            @Qualifier("dbToCsvWriter") ItemWriter<Post> dbToCsvItemWriter) {
        return new StepBuilder("dbToCsvStep", jobRepository)
                .<Post, Post>chunk(chunkSize, transactionManager)
                .reader(dbToCsvItemReader)
                .writer(dbToCsvItemWriter)
                .build();
    }
}
