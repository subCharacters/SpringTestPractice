package com.example.springtestpractice.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 게시글을_등록하고_목록에서_확인한다() throws Exception {
        // 등록
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"통합 제목\",\"content\":\"통합 내용\"}")
                ).andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 목록 확인
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("통합 제목"));
    }

    @Test
    void 게시글을_수정하고_조회한다() throws Exception {
        // 등록
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"통합 제목\",\"content\":\"통합 내용\"}")
                ).andExpect(status().isOk())
                .andReturn();
        // 수정
        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"수정 제목\",\"content\":\"수정 내용\"}"))
                .andExpect(status().isOk());
        // 조회
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("수정 제목"))
                .andExpect(jsonPath("$[0].content").value("수정 내용"));
    }
}
