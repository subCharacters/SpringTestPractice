package com.example.springtestpractice.thread;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PartitionerDbToDbJobConfig {

    private int chunkSize = 2;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public PartitionerDbToDbJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job partitionerDbToDbJob() {
        return new JobBuilder("partitionerDbToDbJob", jobRepository)
                .start(partitionerDbToDbStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step partitionerDbToDbStep() {
        return new StepBuilder("partitionerDbToDbStep", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(partitionerDbToDbReader())
                .processor(partitionerDbToDbProcessor())
                .writer(partitionerDbToDbWriter())
                .build();
    }

    @Bean
    public ItemReader<Customer> partitionerDbToDbReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("partitionerDbToDbReader")
                .resource(new ClassPathResource("input/customers.csv"))
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("id", "firstName", "lastName", "birthDate")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .build();
    }

    @Bean
    public ItemProcessor<Customer,Customer> partitionerDbToDbProcessor() {
        return customer -> {
            customer.setFirstName("[CSV] " + customer.getFirstName());
            return customer;
        };
    }

    @Bean
    public ItemWriter<Customer> partitionerDbToDbWriter() {
        return new FlatFileItemWriterBuilder<Customer>()
                .name("partitionerDbToDbWriter")
                .resource(new FileSystemResource("output/customers.csv"))
                .delimited().delimiter(",")
                .names("id", "firstName", "lastName", "birthDate")
                .build();
    }
}
