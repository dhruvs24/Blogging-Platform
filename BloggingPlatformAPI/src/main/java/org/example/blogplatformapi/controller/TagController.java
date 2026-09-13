package org.example.blogplatformapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.Tag;
import org.example.blogplatformapi.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @PostMapping("/tags")
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @GetMapping("/tags/{tagName}")
    public Tag getTagByName(@PathVariable String tagName) {
        return tagService.getTagByName(tagName);
    }

    @GetMapping("/tags/{tagName}/posts")
    public List<Post> getPostsByTag(@PathVariable String tagName) {
        return tagService.getPostsByTag(tagName);
    }

    @PostMapping("/posts/{postId}/tags")
    public Tag addTagToPost(
            @PathVariable Integer postId,
            @RequestBody Tag tag) {

        return tagService.addTagToPost(postId, tag);
    }

    @GetMapping("/posts/{postId}/tags")
    public List<Tag> getTagsByPost(@PathVariable Integer postId) {
        return tagService.getTagsByPost(postId);
    }

    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    public void removeTagFromPost(
            @PathVariable Integer postId,
            @PathVariable Integer tagId) {

        tagService.removeTagFromPost(postId, tagId);
    }
}