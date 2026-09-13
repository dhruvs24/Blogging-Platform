package org.example.blogplatformapi.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    private Integer id;


    private String content;
    private String title;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Boolean deleted;
    private User user;
}