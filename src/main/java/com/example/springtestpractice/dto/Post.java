package com.example.springtestpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String content;
    private Timestamp createdAt;
}
