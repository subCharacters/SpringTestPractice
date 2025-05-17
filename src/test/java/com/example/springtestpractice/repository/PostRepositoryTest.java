package com.example.springtestpractice.repository;

import com.example.springtestpractice.dto.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// 하나씩 테스트하면 성공하나 전체적으로 돌리면 에러가 남.
// 데이터를 등록하는 부분이 있고 해당 데이터를 조회 할때 사이즈가 1인지 체크하는데
// 게시글을_수정한다()부분에서 이미 데이터가 등록 되어 있어서 게시글을_저장하고_조회한다()에서 에러가 난다.
// 즉 비결정적인 테스트이다.

@DataJdbcTest // Jdbc 관련 컴포넌트만 로딩하여 빠른 테스트 실행이 가능하게 함, 테스트가 끝나면 자동 롤백 처리
@Import({PostRepository.class}) // 테스트 할 클래스를 수동으로 등록해야함.
// @SpringBootTest를 사용한다면 @Import는 필요없음.
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    
    @Test
    void 게시글을_저장하고_조회한다() {
        // given
        Post post = new Post(null, "제목", "내용", null);
        // when
        Long saveResult = postRepository.save(post);
        List<Post> findAllResult = postRepository.findAll();
        // then
        assertThat(saveResult).isEqualTo(1L);
        assertThat(findAllResult).hasSize(1);
        assertThat(findAllResult.get(0).getId()).isEqualTo(1L);
        assertThat(findAllResult.get(0).getTitle()).isEqualTo("제목");
        assertThat(findAllResult.get(0).getContent()).isEqualTo("내용");
    }

    @Test
    void 게시글을_수정한다() {
        // given
        Long id = postRepository.save(new Post(null, "제목", "내용", null));
        // when
        int updated = postRepository.update(new Post(id, "수정제목", "수정내용", null));
        List<Post> findAllResult = postRepository.findAll();
        // then
        assertThat(updated).isEqualTo(1);
        assertThat(findAllResult.get(0).getTitle()).isEqualTo("수정제목");
        assertThat(findAllResult.get(0).getContent()).isEqualTo("수정내용");
    }
}