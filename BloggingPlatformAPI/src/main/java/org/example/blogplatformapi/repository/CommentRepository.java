package org.example.blogplatformapi.repository;

import org.example.blogplatformapi.repository.persistence.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByPostEntityId(Integer postId);

    Optional<CommentEntity> findByIdAndPostEntityId(
            Integer commentId,
            Integer postId
    );
}