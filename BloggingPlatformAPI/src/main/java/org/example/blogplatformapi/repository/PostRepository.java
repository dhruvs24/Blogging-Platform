package org.example.blogplatformapi.repository;

import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query("""
        SELECT p
        FROM PostEntity p
        JOIN p.tagEntities t
        WHERE t.tagName = :tagName
        """)
    List<PostEntity> findPostsByTagName(
            @Param("tagName") String tagName
    );
}