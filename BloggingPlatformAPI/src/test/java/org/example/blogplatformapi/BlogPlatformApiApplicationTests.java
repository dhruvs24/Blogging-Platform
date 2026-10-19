package org.example.blogplatformapi;

import org.example.blogplatformapi.repository.CommentRepository;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.TagRepository;
import org.example.blogplatformapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
class BlogPlatformApiApplicationTests {

    @MockitoBean
    CommentRepository commentRepository;
    @MockitoBean
    PostRepository postRepository;
    @MockitoBean
    TagRepository tagRepository;
    @MockitoBean
    UserRepository userRepository;

    @Test
    void contextLoads() {
    }

}
