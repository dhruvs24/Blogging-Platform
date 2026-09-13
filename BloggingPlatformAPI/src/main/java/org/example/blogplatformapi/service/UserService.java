package org.example.blogplatformapi.service;

import lombok.AllArgsConstructor;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.User;

import org.example.blogplatformapi.repository.UserRepository;
import org.example.blogplatformapi.repository.persistence.PostEntity;
import org.example.blogplatformapi.repository.persistence.UserEntity;
import org.example.blogplatformapi.util.EntityModelTransformer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;



    // CREATE
    public User createUser(User user) {

        UserEntity entity = EntityModelTransformer.toUserEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        return EntityModelTransformer.toUser(savedEntity);
    }

    // READ ALL
    public List<User> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(EntityModelTransformer::toUser)
                .toList();
    }

    // READ ONE
    public User getUserById(Integer id) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return EntityModelTransformer.toUser(entity);
    }

    // UPDATE
    public User updateUser(Integer id, User updatedUser) {

        UserEntity existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingEntity.setFullName(updatedUser.getFull_name());
        existingEntity.setEmail(updatedUser.getEmail());
        existingEntity.setUsername(updatedUser.getUsername());

        UserEntity savedEntity = userRepository.save(existingEntity);

        return EntityModelTransformer.toUser(savedEntity);
    }

    // DELETE
    public void deleteUser(Integer id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }

    public List<Post> getUserPosts(Integer id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userEntity.getPostEntities()
                .stream()
                .map(EntityModelTransformer::toPost)
                .toList();
    }

}