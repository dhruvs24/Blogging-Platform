package org.example.blogplatformapi.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.blogplatformapi.model.Comment;
import org.example.blogplatformapi.repository.CommentRepository;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.UserRepository;
import org.example.blogplatformapi.repository.persistence.CommentEntity;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;
import org.example.blogplatformapi.util.EntityModelTransformer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;



    // CREATE COMMENT ON POST
    public Comment createComment(Integer postId, Comment comment) {

        UserEntity userEntity = userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        CommentEntity entity =
                EntityModelTransformer.toCommentEntity(comment);

        entity.setUserEntity(userEntity);
        entity.setPostEntity(postEntity);

        CommentEntity savedEntity =
                commentRepository.save(entity);

        return EntityModelTransformer.toComment(savedEntity);
    }

    // GET ALL COMMENTS FOR POST
    public List<Comment> getCommentsByPostId(Integer postId) {

        return commentRepository.findByPostEntityId(postId)
                .stream()
                .map(EntityModelTransformer::toComment)
                .toList();
    }

    // GET ONE COMMENT FROM POST
    public Comment getCommentById(
            Integer postId,
            Integer commentId) {

        CommentEntity entity =
                commentRepository
                        .findByIdAndPostEntityId(commentId, postId)
                        .orElseThrow(() ->
                                new RuntimeException("Comment not found"));

        return EntityModelTransformer.toComment(entity);
    }

    // UPDATE COMMENT
    public Comment updateComment(
            Integer postId,
            Integer commentId,
            Comment updatedComment) {

        CommentEntity existingEntity =
                commentRepository
                        .findByIdAndPostEntityId(commentId, postId)
                        .orElseThrow(() ->
                                new RuntimeException("Comment not found"));

        existingEntity.setContent(updatedComment.getContent());
        existingEntity.setUpdated_at(LocalDateTime.now());

        CommentEntity savedEntity =
                commentRepository.save(existingEntity);

        return EntityModelTransformer.toComment(savedEntity);
    }

    // DELETE COMMENT
    public void deleteComment(
            Integer postId,
            Integer commentId) {

        CommentEntity entity =
                commentRepository
                        .findByIdAndPostEntityId(commentId, postId)
                        .orElseThrow(() ->
                                new RuntimeException("Comment not found"));

        commentRepository.delete(entity);
    }
}