package org.example.blogplatformapi.util;

import org.example.blogplatformapi.model.Comment;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.Tag;
import org.example.blogplatformapi.model.User;
import org.example.blogplatformapi.repository.persistence.CommentEntity;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.TagEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;

public class EntityModelTransformer {


    public static User toUser(UserEntity userEntity) {

        return User.builder()
                .id(userEntity.getId())
                .full_name(userEntity.getFullName())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .created_at(userEntity.getCreatedAt())
                .build();
    }


    public static UserEntity toUserEntity(User user) {

        return UserEntity.builder()
                .id(user.getId())
                .fullName(user.getFull_name())
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getCreated_at())
                .build();
    }


    public static Post toPost(PostEntity postEntity) {

        UserEntity userEntity = postEntity.getUserEntity();
        User user = toUser(userEntity);

        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .created_at(postEntity.getCreated_at())
                .updated_at(postEntity.getUpdated_at())
                .deleted(postEntity.getDeleted())
                .user(user)
                .build();
    }

    public static PostEntity toPostEntity(Post post) {

        return PostEntity.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .deleted(post.getDeleted())
                .build();
    }

    // CommentEntity to Comment
    public static Comment toComment(CommentEntity commentEntity) {

        return Comment.builder()
                .id(commentEntity.getId())
                .content(commentEntity.getContent())
                .created_at(commentEntity.getCreated_at())
                .updated_at(commentEntity.getUpdated_at())
                .userId(commentEntity.getUserEntity().getId())
                .postId(commentEntity.getPostEntity().getId())
                .build();
    }

    // Comment to CommentEntity
    public static CommentEntity toCommentEntity(Comment comment) {

        return CommentEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .build();
    }

    public static Tag toTag(TagEntity tagEntity) {

        return Tag.builder()
                .id(tagEntity.getId())
                .tag_name(tagEntity.getTagName())
                .build();
    }

    public static TagEntity toTagEntity(Tag tag) {

        return TagEntity.builder()
                .id(tag.getId())
                .tagName(tag.getTag_name())
                .build();
    }
}