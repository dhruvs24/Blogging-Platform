package org.example.blogplatformapi.service;

import lombok.AllArgsConstructor;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.Tag;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.TagRepository;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.TagEntity;
import org.example.blogplatformapi.util.EntityModelTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;


    public Tag createTag(Tag tag) {

        TagEntity entity = EntityModelTransformer.toTagEntity(tag);
        TagEntity savedEntity = tagRepository.save(entity);
        return EntityModelTransformer.toTag(savedEntity);
    }

    public Tag getTagByName(String tagName) {

        TagEntity entity = tagRepository
                .findByTagName(tagName)
                .orElseThrow(() ->
                        new RuntimeException("Tag not found"));

        return EntityModelTransformer.toTag(entity);
    }

    public List<Post> getPostsByTag(String tagName) {

        List<PostEntity> entities =
                postRepository.findPostsByTagName(tagName);

        return entities.stream()
                .map(EntityModelTransformer::toPost)
                .toList();
    }

    public Tag addTagToPost(Integer postId, Tag tag) {

        PostEntity postEntity =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new RuntimeException("Post not found"));

        TagEntity tagEntity =
                tagRepository.findByTagName(tag.getTag_name())
                        .orElseGet(() ->
                                tagRepository.save(
                                        EntityModelTransformer.toTagEntity(tag)
                                )
                        );

        if (!postEntity.getTagEntities().contains(tagEntity)) {
            postEntity.getTagEntities().add(tagEntity);
            postRepository.save(postEntity);
        }

        return EntityModelTransformer.toTag(tagEntity);
    }

    public List<Tag> getTagsByPost(Integer postId) {

        PostEntity postEntity =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new RuntimeException("Post not found"));

        return postEntity.getTagEntities()
                .stream()
                .map(EntityModelTransformer::toTag)
                .toList();
    }

    public void removeTagFromPost(
            Integer postId,
            Integer tagId) {

        PostEntity postEntity =
                postRepository.findById(postId)
                        .orElseThrow(() ->
                                new RuntimeException("Post not found"));

        TagEntity tagEntity =
                tagRepository.findById(tagId)
                        .orElseThrow(() ->
                                new RuntimeException("Tag not found"));

        postEntity.getTagEntities().remove(tagEntity);

        postRepository.save(postEntity);
    }
}