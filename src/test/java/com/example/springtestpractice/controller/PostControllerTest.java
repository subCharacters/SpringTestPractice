package com.example.springtestpractice.controller;

import com.example.springtestpractice.dto.Post;
import com.example.springtestpractice.repository.PostRepository;
import com.example.springtestpractice.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
@MockitoBean(types = PostRepository.class) // MockBean은 3.4? 부터 비추천됨.
@Import(PostService.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;


    @Test
    void 게시글을_등록한다() throws Exception {
        // given
        given(postRepository.save(any())).willReturn(1L);
        // when
        // then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"테스트 제목\",\"content\":\"테스트 내용\"}")
                ).andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void 게시글_목록을_조회한다() throws Exception {
        // given
        given(postRepository.findAll()).willReturn(List.of(new Post(1L, "제목", "내용", null)));
        // when
        // then
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목"))
                .andExpect(jsonPath("$[0].content").value("내용"))
                .andExpect(jsonPath("$.length()").value(1));
    /*
        첫 번째 요소의 title	$[0].title
        전체 배열의 크기	$.length()
        두 번째 요소의 content 값	$[1].content
        모든 title 값 중에 "제목" 존재	$[*].title + .value(...) 대신 .contains(...)
    */
    }

    @Test
    void 게시글을_수정한다() throws Exception {
        // given
        given(postRepository.update(any())).willReturn(1);
        // when
        // then
        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"테스트 제목\",\"content\":\"테스트 내용\"}"))
                .andExpect(status().isOk());
    }

}