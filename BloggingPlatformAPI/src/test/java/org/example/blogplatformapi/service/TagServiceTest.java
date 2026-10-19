package org.example.blogplatformapi.service;

import org.example.blogplatformapi.model.Tag;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.repository.persistence.TagEntity;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.TagRepository;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.persistence.UserEntity;

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
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private TagService tagService;

    private Tag createTagObject() {
        return Tag.builder()
                .tag_name("Java")
                .build();
    }

    private TagEntity createTagEntity(int id, String tagName) {
        return TagEntity.builder()
                .id(id)
                .tagName(tagName)
                .build();
    }

    private PostEntity createPostEntity(int id) {
        UserEntity user = UserEntity.builder()
                .id(id)
                .fullName("Test User")
                .email("test@example.com")
                .username("testuser")
                .postEntities(new ArrayList<>())
                .build();

        return PostEntity.builder()
                .id(1)
                .userEntity(user)
                .tagEntities(new ArrayList<>())
                .build();
    }

    @Test
    void createTag_returnsSavedTag() {
        Tag tag = createTagObject();

        when(tagRepository.save(any(TagEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tag result = tagService.createTag(tag);

        assertEquals("Java", result.getTag_name());
        verify(tagRepository).save(any(TagEntity.class));
    }

    @Test
    void getTagByName_returnsTag() {
        TagEntity entity = createTagEntity(1, "Java");

        when(tagRepository.findByTagName("Java"))
                .thenReturn(Optional.of(entity));

        Tag result = tagService.getTagByName("Java");

        assertEquals("Java", result.getTag_name());
        verify(tagRepository).findByTagName("Java");
    }

    @Test
    void getTagByName_throwsExceptionWhenTagNotFound() {
        when(tagRepository.findByTagName("Java"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> tagService.getTagByName("Java"));
    }

    @Test
    void getPostsByTag_returnsPosts() {
        PostEntity entity1 = createPostEntity(1);
        PostEntity entity2 = createPostEntity(2);

        when(postRepository.findPostsByTagName("Java"))
                .thenReturn(List.of(entity1, entity2));

        List<Post> result = tagService.getPostsByTag("Java");

        assertEquals(2, result.size());
        verify(postRepository).findPostsByTagName("Java");
    }

    @Test
    void getPostsByTag_returnsEmptyListWhenNoPostsMatch() {
        when(postRepository.findPostsByTagName("Java"))
                .thenReturn(new ArrayList<>());

        List<Post> result = tagService.getPostsByTag("Java");

        assertTrue(result.isEmpty());
    }

    @Test
    void addTagToPost_addsExistingTagToPost() {
        PostEntity post = createPostEntity(1);
        TagEntity tagEntity = createTagEntity(1, "Java");

        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));
        when(tagRepository.findByTagName("Java"))
                .thenReturn(Optional.of(tagEntity));

        Tag result = tagService.addTagToPost(1, createTagObject());

        assertEquals("Java", result.getTag_name());
        assertTrue(post.getTagEntities().contains(tagEntity));
        verify(postRepository).save(post);
        verify(tagRepository, never()).save(any(TagEntity.class));
    }

    @Test
    void addTagToPost_createsTagWhenItDoesNotExist() {
        PostEntity post = createPostEntity(1);
        TagEntity tagEntity = createTagEntity(1, "Java");

        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));
        when(tagRepository.findByTagName("Java"))
                .thenReturn(Optional.empty());
        when(tagRepository.save(any(TagEntity.class)))
                .thenReturn(tagEntity);

        Tag result = tagService.addTagToPost(1, createTagObject());

        assertEquals("Java", result.getTag_name());
        assertTrue(post.getTagEntities().contains(tagEntity));
        verify(tagRepository).save(any(TagEntity.class));
        verify(postRepository).save(post);
    }

    @Test
    void addTagToPost_doesNotAddDuplicateTag() {
        PostEntity post = createPostEntity(1);
        TagEntity tagEntity = createTagEntity(1, "Java");
        post.getTagEntities().add(tagEntity);

        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));
        when(tagRepository.findByTagName("Java"))
                .thenReturn(Optional.of(tagEntity));

        tagService.addTagToPost(1, createTagObject());

        assertEquals(1, post.getTagEntities().size());
        verify(postRepository, never()).save(post);
    }

    @Test
    void addTagToPost_throwsExceptionWhenPostNotFound() {
        when(postRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> tagService.addTagToPost(1, createTagObject()));
    }

    @Test
    void getTagsByPost_returnsTags() {
        PostEntity post = createPostEntity(1);
        TagEntity tagEntity1 = createTagEntity(1, "Java");
        TagEntity tagEntity2 = createTagEntity(2, "C++");

        post.getTagEntities().add(tagEntity1);
        post.getTagEntities().add(tagEntity2);

        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));

        List<Tag> result = tagService.getTagsByPost(1);

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTag_name());
    }

    @Test
    void getTagsByPost_throwsExceptionWhenPostNotFound() {
        when(postRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> tagService.getTagsByPost(1));
    }

    @Test
    void removeTagFromPost_removesTag() {
        PostEntity post = createPostEntity(1);
        TagEntity tagEntity = createTagEntity(1, "Java");
        post.getTagEntities().add(tagEntity);

        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));
        when(tagRepository.findById(1))
                .thenReturn(Optional.of(tagEntity));

        tagService.removeTagFromPost(1, 1);

        assertFalse(post.getTagEntities().contains(tagEntity));
        verify(postRepository).save(post);
    }

    @Test
    void removeTagFromPost_throwsExceptionWhenPostNotFound() {
        when(postRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> tagService.removeTagFromPost(1, 1));

        verify(tagRepository, never()).findById(1);
    }

    @Test
    void removeTagFromPost_throwsExceptionWhenTagNotFound() {
        when(postRepository.findById(1))
                .thenReturn(Optional.of(createPostEntity(1)));
        when(tagRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> tagService.removeTagFromPost(1, 1));

        verify(postRepository, never()).save(any(PostEntity.class));
    }
}