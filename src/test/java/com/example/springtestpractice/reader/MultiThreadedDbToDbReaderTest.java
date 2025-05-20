package com.example.springtestpractice.reader;

import com.example.springtestpractice.thread.Customer;
import com.example.springtestpractice.thread.MultiThreadedDbToDbJobConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJdbcTest jobRepository나 transactionManager등을 불러오지 못하여 에러가 발생.
// MultiThreadedDbToDbJobConfig 클래스를 통채로 불러오기에 발생하는 이슈.
@SpringBootTest // 테스트가 무거워지는 치명적 단점이 존재함. 따라서 리드 클래스는 분리하는게 좋음.
// SRP관점에서도 필요.
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(MultiThreadedDbToDbJobConfig.class)
public class MultiThreadedDbToDbReaderTest {

    @Autowired
    @Qualifier("multiThreadedDbToDbReader")
    private JdbcPagingItemReader<Customer> reader;

    @BeforeEach
    void openReader() {
        reader.open(new ExecutionContext());
    }

    @AfterEach
    void closeReader() {
        reader.close();
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sql/insert_customer.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    void multiThreadedDbToDbReader의_read가_정상적으로_작동한다() throws Exception {
        List<Customer> results = new ArrayList<>();
        Customer customer;
        while ((customer = reader.read()) != null) {
            results.add(customer);
        }

        assertThat(results.size()).isEqualTo(10);
    }
}
