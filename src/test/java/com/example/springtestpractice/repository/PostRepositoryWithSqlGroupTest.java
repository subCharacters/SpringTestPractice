package com.example.springtestpractice.repository;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// 1. SqlGroup 어노테이션 사용
// 기본적으로 apllication.yml을 사용하지만 test 환경과 운용환경을 분리해야한다.
// 이때 test안에 resources폴더 안에 application-test.yml을 만들고 설정을 따로 하면 application.yml에 추가된다.
// 테스트에 사용할 스키마 정의 sql을 만들수도 있고 로그 레벨을 따로 설정하거나 mock서버 url을 따로 설정하는 등이 가능하다.
@ActiveProfiles("test")
@DataJdbcTest
@Import(PostRepository.class)
// sql 파일을 따로 두어 테스트에 필요한 데이터를 넣거나 뺄 수 있다.
// BeforeEach나 AfterEach어노테이션을 사용해도 좋고 아래와 같이 정의 할 수도 있다.
// executionPhase로 언제 sql을 실행 할건지 정의가 가능하다.
// 클래스 단위가 아니라 각 메서드 단위로 어노테이션을 붙이면 각각 실행도 가능하다.
@SqlGroup({
        @Sql(scripts = "/sql/insert_posts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostRepositoryWithSqlGroupTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    void 데이터를_조회할_수_있다() {
        // given
        // when
        List<Post> posts = postRepository.findAll();
        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("사전 등록 제목");
        assertThat(posts.get(0).getContent()).isEqualTo("사전 등록 내용");
    }
}
