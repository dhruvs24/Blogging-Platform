package org.example.blogplatformapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatformapi.model.Post;
import org.example.blogplatformapi.model.User;
import org.example.blogplatformapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}/posts")
    public List<Post> getUserPosts(@PathVariable Integer id) {
        return userService.getUserPosts(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}