package org.example.blogplatformapi.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Integer id;
    private String full_name;
    private String email;
    private String username;
    private LocalDateTime created_at;

    private List<Post> posts;
}