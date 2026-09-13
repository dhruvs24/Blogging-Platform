package org.example.blogplatformapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatformapi.model.Comment;
import org.example.blogplatformapi.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;



    // CREATE
    @PostMapping
    public Comment createComment(
            @PathVariable Integer postId,
            @RequestBody Comment comment) {

        return commentService.createComment(postId, comment);
    }

    // GET ALL COMMENTS FOR A POST
    @GetMapping
    public List<Comment> getCommentsByPostId(
            @PathVariable Integer postId) {

        return commentService.getCommentsByPostId(postId);
    }

    // GET ONE COMMENT
    @GetMapping("/{commentId}")
    public Comment getCommentById(
            @PathVariable Integer postId,
            @PathVariable Integer commentId) {

        return commentService.getCommentById(postId, commentId);
    }

    // UPDATE
    @PutMapping("/{commentId}")
    public Comment updateComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @RequestBody Comment comment) {

        return commentService.updateComment(postId, commentId, comment);
    }

    // DELETE
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId) {

        commentService.deleteComment(postId, commentId);
    }
}