package org.example.blogplatformapi.service;

import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.User;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.UserRepository;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void createPost_throwsExceptionWhenUserNotFound() {

        User user = User.builder()
                .id(1000)
                .build();

        Post post = Post.builder()
                .title("Test Post")
                .content("Test Content")
                .user(user)
                .build();

        when(userRepository.findById(1000))
                .thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.createPost(post)
        );

        assertEquals("User not found", exception.getMessage());
        verify(postRepository, never()).save(any(PostEntity.class));
    }


    @Test
    void createPost_returnsCreatedPostWhenUserExists() {

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .build();

        User user = User.builder()
                .id(1)
                .build();

        Post post = Post.builder()
                .title("Test Post")
                .content("Test Content")
                .user(user)
                .build();

        PostEntity savedEntity = PostEntity.builder()
                .id(10)
                .title("Test Post")
                .content("Test Content")
                .userEntity(userEntity)
                .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(userEntity));

        when(postRepository.save(any(PostEntity.class)))
                .thenReturn(savedEntity);

        Post result = postService.createPost(post);

        assertEquals(10, result.getId());
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test Content", result.getContent());
        verify(userRepository).findById(1);
        verify(postRepository).save(any(PostEntity.class));
    }



    @Test
    void getPostById_returnsPostWhenFound() {

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .build();

        PostEntity postEntity = PostEntity.builder()
                .id(10)
                .title("Test Post")
                .content("Test Content")
                .userEntity(userEntity)
                .build();

        when(postRepository.findById(10))
                .thenReturn(Optional.of(postEntity));

        Post result = postService.getPostById(10);

        assertEquals(10, result.getId());
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals(1, result.getUser().getId());
        verify(postRepository).findById(10);
    }


    @Test
    void getPostById_throwsExceptionWhenPostNotFound() {

        when(postRepository.findById(999))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.getPostById(999)
        );

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findById(999);
    }


    @Test
    void getAllPosts_returnsAllPosts() {

        UserEntity userEntity1 = UserEntity.builder()
                .id(1)
                .build();
        UserEntity userEntity2 = UserEntity.builder()
                .id(2)
                .build();

        PostEntity post1 = PostEntity.builder()
                .id(10)
                .title("First Post")
                .content("First Content")
                .userEntity(userEntity1)
                .build();

        PostEntity post2 = PostEntity.builder()
                .id(11)
                .title("Second Post")
                .content("Second Content")
                .userEntity(userEntity2)
                .build();

        when(postRepository.findAll())
                .thenReturn(List.of(post1, post2));


        List<Post> result = postService.getAllPosts();


        assertEquals(2, result.size());
        assertEquals("First Post", result.get(0).getTitle());
        assertEquals("Second Post", result.get(1).getTitle());
        verify(postRepository).findAll();
    }

    @Test
    void updatePost_updatesPostWhenFound() {

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .build();

        PostEntity existingEntity = PostEntity.builder()
                .id(10)
                .title("Old Title")
                .content("Old Content")
                .userEntity(userEntity)
                .build();

        Post updatedPost = Post.builder()
                .title("New Title")
                .content("New Content")
                .build();

        when(postRepository.findById(10))
                .thenReturn(Optional.of(existingEntity));

        when(postRepository.save(existingEntity))
                .thenReturn(existingEntity);

        Post result = postService.updatePost(10, updatedPost);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
        verify(postRepository).findById(10);
        verify(postRepository).save(existingEntity);
    }

    @Test
    void deletePost_deletesPostWhenFound() {

        when(postRepository.existsById(10)).thenReturn(true);

        postService.deletePost(10);

        verify(postRepository).existsById(10);
        verify(postRepository).deleteById(10);
    }

    @Test
    void deletePost_throwsExceptionWhenPostNotFound() {

        when(postRepository.existsById(999)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> postService.deletePost(999)
        );

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).existsById(999);
        verify(postRepository, never()).deleteById(999);
    }
}