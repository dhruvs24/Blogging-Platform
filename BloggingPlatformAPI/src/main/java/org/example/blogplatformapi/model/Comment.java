package org.example.blogplatformapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    private Integer id;
    private String content;
    private Integer likes;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Boolean deleted;
    private Integer userId;
    private Integer postId;
}