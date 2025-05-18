package com.example.springtestpractice.integration;

import com.example.springtestpractice.job.CsvToDbJobConfig;
import com.example.springtestpractice.job.DbToCsvJobConfig;
import com.example.springtestpractice.reader.CsvToDbItemReader;
import com.example.springtestpractice.reader.DbToCsvItemReader;
import com.example.springtestpractice.writer.CsvToDbItemWriter;
import com.example.springtestpractice.writer.DbToCsvItemWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "spring.batch.job.name=dbToCsvJob")
@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({DbToCsvJobConfig.class, DbToCsvItemReader.class, DbToCsvItemWriter.class, CsvToDbJobConfig.class, CsvToDbItemReader.class, CsvToDbItemWriter.class})
public class BatchJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("dbToCsvJob")
    private Job dbToCsvJob;

    @Test
    void dbToCsvJob이_정상_완료된다() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // when
        jobLauncherTestUtils.setJob(dbToCsvJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assert
    }

    @Test
    void csvToDbJob이_정상_완료된다() throws Exception {

    }
}
