package org.example.blogplatformapi.repository;

import org.example.blogplatformapi.repository.persistence.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Optional<TagEntity> findByTagName(String tagName);
}