package com.example.springtestpractice.service;

import com.example.springtestpractice.dto.Post;
import com.example.springtestpractice.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Long createPost(String title, String content) {
        Post post = new Post(null, title, content, null);
        return postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public boolean updatePost(Long id, String title, String content) {
        Post post = new Post(id, title, content, null);
        int updated = postRepository.update(post);
        return updated > 0;
    }
}
