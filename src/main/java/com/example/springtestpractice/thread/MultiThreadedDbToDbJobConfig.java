package com.example.springtestpractice.thread;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiThreadedDbToDbJobConfig {

    private int chunkSize = 2;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    public MultiThreadedDbToDbJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
    }

    @Bean
    public Job multiThreadedDbToDbJob() {
        return new JobBuilder("multiThreadedDbToDbJob", jobRepository)
                .start(multiThreadedDbToDbStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step multiThreadedDbToDbStep() {
        return new StepBuilder("multiThreadedDbToDbStep", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(multiThreadedDbToDbReader())
                .processor(multiThreadedDbToDbProcessor())
                .writer(multiThreadedDbToDbWriter())
                .taskExecutor(multiThreadedTaskExecutor())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Customer> multiThreadedDbToDbReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setRowMapper(new BeanPropertyRowMapper<>(Customer.class));

        OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthdate");
        queryProvider.setFromClause("from customer");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);
        return reader;
    }

    @Bean
    public ItemProcessor<Customer, Customer> multiThreadedDbToDbProcessor() {
        return customer -> {
            customer.setFirstName("[MT] " + customer.getFirstName());
            return customer;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Customer> multiThreadedDbToDbWriter() {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO customer_mt (id, firstName, lastName, birthdate) VALUES (:id, :firstName, :lastName, :birthdate)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public TaskExecutor multiThreadedTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setThreadNamePrefix("multiThreadedTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
