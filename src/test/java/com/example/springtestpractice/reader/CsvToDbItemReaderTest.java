package com.example.springtestpractice.reader;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = CsvToDbItemReader.class) // bean등록
@TestPropertySource(properties = "job.input.path=test-input") // 단위 테스트에서 필요. CsvToDbItemReader에 Value로드에 yml이 적용안되기 때문.
class CsvToDbItemReaderTest {

    @Qualifier("csvToDbReader")
    @Autowired
    private FlatFileItemReader<Post> reader;

    @Test
    void csvToDbItemReader의_read가_정상적으로_작동한다() throws Exception {
        reader.open(new ExecutionContext());
        Post item1 = reader.read();
        Post item2 = reader.read();
        reader.close();

        assertNotNull(item1);
        assertNotNull(item2);
        assertEquals("제목1", item1.getTitle());
        assertEquals("내용1", item1.getContent());
        assertEquals("제목2", item2.getTitle());
        assertEquals("내용2", item2.getContent());
    }
}