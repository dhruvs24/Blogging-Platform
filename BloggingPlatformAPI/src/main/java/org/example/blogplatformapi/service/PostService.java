package org.example.blogplatformapi.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.repository.PostRepository;
import org.example.blogplatformapi.repository.UserRepository;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;
import org.example.blogplatformapi.util.EntityModelTransformer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(Post post) {

        UserEntity userEntity = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostEntity entity = EntityModelTransformer.toPostEntity(post);

        entity.setUserEntity(userEntity);

        PostEntity savedEntity = postRepository.save(entity);

        return EntityModelTransformer.toPost(savedEntity);
    }

    // READ ALL
    public List<Post> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(EntityModelTransformer::toPost)
                .toList();
    }

    // READ ONE
    public Post getPostById(Integer id) {

        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return EntityModelTransformer.toPost(entity);
    }

    // UPDATE
    public Post updatePost(Integer id, Post updatedPost) {

        PostEntity existingEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingEntity.setTitle(updatedPost.getTitle());
        existingEntity.setContent(updatedPost.getContent());
        existingEntity.setUpdated_at(LocalDateTime.now());

        PostEntity savedEntity = postRepository.save(existingEntity);

        return EntityModelTransformer.toPost(savedEntity);
    }

    // DELETE
    public void deletePost(Integer id) {

        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }

        postRepository.deleteById(id);
    }


}
