package com.example.springtestpractice.controller;

import com.example.springtestpractice.dto.Post;
import com.example.springtestpractice.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Map<String, String> body) {
        Long id = postService.createPost(body.get("title"), body.get("content"));
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public List<Post> list() {
        return postService.getPosts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        boolean updated = postService.updatePost(id, body.get("title"), body.get("content"));
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
