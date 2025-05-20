package com.example.springtestpractice.writer;

import com.example.springtestpractice.thread.Customer;
import com.example.springtestpractice.thread.MultiThreadedDbToDbJobConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("Test")
@Import(MultiThreadedDbToDbJobConfig.class)
// @Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MultiThreadedDbToDbWriterTest {

    @Autowired
    @Qualifier("multiThreadedDbToDbWriter")
    private JdbcBatchItemWriter<Customer> writer;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        writer.afterPropertiesSet();
    }

    @Test
    void multiThreadedDbToDbWriter가_DB에_정상적으로_쓰기_처리를_한다() throws Exception {
        // given
        Customer customer = new Customer(999, "Test", "User", "2020-01-01");
        Chunk<Customer> chunk = new Chunk<>(List.of(customer));

        // when
        writer.write(chunk);

        // then
        try(Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer_mt WHERE id = 999");
            ResultSet rs = ps.executeQuery();

            assertThat(rs.next()).isTrue();
            assertThat(rs.getLong("id")).isEqualTo(999);
            assertThat(rs.getString("firstName")).isEqualTo("Test");
            assertThat(rs.getString("lastName")).isEqualTo("User");
            assertThat(rs.getString("birthdate")).isEqualTo("2020-01-01");
        }
    }
}
