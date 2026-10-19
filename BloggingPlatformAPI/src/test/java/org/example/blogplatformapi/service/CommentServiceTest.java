package org.example.blogplatformapi.service;

import org.example.blogplatformapi.model.Comment;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.UserRepository;
import org.example.blogplatformapi.repository.CommentRepository;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;
import org.example.blogplatformapi.repository.persistence.CommentEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment createCommentObject() {
        return Comment.builder()
                .content("Test comment")
                .userId(1)
                .build();
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(1)
                .fullName("Test User")
                .email("test@example.com")
                .username("testuser")
                .postEntities(new ArrayList<>())
                .build();
    }

    private PostEntity createPostEntity() {
        return PostEntity.builder()
                .id(10)
                .userEntity(createUserEntity())
                .build();
    }

    private CommentEntity createCommentEntity(int id) {
        return CommentEntity.builder()
                .id(id)
                .content("Test comment")
                .userEntity(createUserEntity())
                .postEntity(createPostEntity())
                .build();
    }

    @Test
    void createComment_returnsSavedComment() {
        Comment comment = createCommentObject();

        UserEntity user = createUserEntity();
        PostEntity post = createPostEntity();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(postRepository.findById(10))
                .thenReturn(Optional.of(post));

        when(commentRepository.save(any(CommentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.createComment(10, comment);

        assertEquals("Test comment", result.getContent());

        verify(userRepository).findById(1);
        verify(postRepository).findById(10);
        verify(commentRepository).save(any(CommentEntity.class));
    }

    @Test
    void createComment_throwsExceptionWhenUserNotFound() {
        Comment comment = createCommentObject();

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.createComment(10, comment)
        );

        assertEquals("User not found", exception.getMessage());
        verify(postRepository, never()).findById(anyInt());
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    void createComment_throwsExceptionWhenPostNotFound() {
        Comment comment = createCommentObject();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(createUserEntity()));

        when(postRepository.findById(10))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.createComment(10, comment)
        );

        assertEquals("Post not found", exception.getMessage());
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    void getCommentsByPostId_returnsComments() {
        CommentEntity entity1 = createCommentEntity(1);
        CommentEntity entity2 = createCommentEntity(5);

        when(commentRepository.findByPostEntityId(10))
                .thenReturn(List.of(entity1, entity2));

        List<Comment> result = commentService.getCommentsByPostId(10);

        assertEquals(2, result.size());
        assertEquals("Test comment", result.get(0).getContent());
        verify(commentRepository).findByPostEntityId(10);
    }

    @Test
    void getCommentsByPostId_returnsEmptyListWhenNoCommentsExist() {
        when(commentRepository.findByPostEntityId(10))
                .thenReturn(List.of());

        List<Comment> result = commentService.getCommentsByPostId(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCommentById_returnsCommentWhenFound() {
        CommentEntity entity = createCommentEntity(1);

        when(commentRepository.findByIdAndPostEntityId(5, 10))
                .thenReturn(Optional.of(entity));

        Comment result = commentService.getCommentById(10, 5);

        assertEquals("Test comment", result.getContent());

        verify(commentRepository).findByIdAndPostEntityId(5, 10);
    }

    @Test
    void getCommentById_throwsExceptionWhenNotFound() {
        when(commentRepository.findByIdAndPostEntityId(99, 10))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.getCommentById(10, 99)
        );

        assertEquals("Comment not found", exception.getMessage());
    }

    @Test
    void updateComment_updatesExistingComment() {
        CommentEntity entity = createCommentEntity(1);

        Comment updatedComment = Comment.builder()
                .content("Updated comment")
                .build();

        when(commentRepository.findByIdAndPostEntityId(5, 10))
                .thenReturn(Optional.of(entity));

        when(commentRepository.save(any(CommentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.updateComment(
                10, 5, updatedComment
        );

        assertEquals("Updated comment", result.getContent());
        verify(commentRepository).save(any(CommentEntity.class));
    }

    @Test
    void updateComment_throwsExceptionWhenNotFound() {
        Comment updatedComment = Comment.builder()
                .content("Updated comment")
                .build();

        when(commentRepository.findByIdAndPostEntityId(99, 10))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.updateComment(10, 99, updatedComment)
        );

        assertEquals("Comment not found", exception.getMessage());
        verify(commentRepository, never()).save(any(CommentEntity.class));
    }

    @Test
    void deleteComment_deletesExistingComment() {
        CommentEntity entity = createCommentEntity(1);

        when(commentRepository.findByIdAndPostEntityId(5, 10))
                .thenReturn(Optional.of(entity));

        commentService.deleteComment(10, 5);

        verify(commentRepository).delete(entity);
    }

    @Test
    void deleteComment_throwsExceptionWhenNotFound() {
        when(commentRepository.findByIdAndPostEntityId(99, 10))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> commentService.deleteComment(10, 99)
        );

        assertEquals("Comment not found", exception.getMessage());
        verify(commentRepository, never()).delete(any(CommentEntity.class));
    }
}