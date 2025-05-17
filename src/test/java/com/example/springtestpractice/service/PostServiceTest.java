package com.example.springtestpractice.service;

import com.example.springtestpractice.dto.Post;
import com.example.springtestpractice.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class) // @Mock, @InjectMocks, @Captor 등의 Mockito 관련 어노테이션을 JUnit5에서 인식
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 게시글을_저장한다() {
        // given
        String title = "title";
        String content = "content";
        given(postRepository.save(any(Post.class))).willReturn(1L);
        // when
        Long id = postService.createPost(title, content);

        // then
        assertEquals(1L, id); // 실제 값을 비교.
        verify(postRepository).save(any(Post.class)); // 호출되었는지 확인.
    }

    @Test
    void 게시글_목록을_조회한다() {
        // given
        List<Post> posts = List.of(
                new Post(1L, "제목1", "내용1", null),
                new Post(2L, "제목2", "내용2", null)
        );
        given(postRepository.findAll()).willReturn(posts);
        // when
        List<Post> result = postService.getPosts();
        // then
        assertThat(result).hasSize(2);
        verify(postRepository).findAll();
    }

    @Test
    void 게시글을_수정한다() {
        // given
        given(postRepository.update(any(Post.class))).willReturn(1);
        // when
        boolean updated = postService.updatePost(1L, "수정된 제목", "수정된 내용");
        // then
        assertThat(updated).isTrue();
        verify(postRepository).update(any(Post.class));
    }
}