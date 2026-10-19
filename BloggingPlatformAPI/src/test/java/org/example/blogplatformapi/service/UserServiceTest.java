package org.example.blogplatformapi.service;

import org.example.blogplatformapi.model.User;
import org.example.blogplatformapi.repository.persistence.UserEntity;
import org.example.blogplatformapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User createUserObject() {
        return User.builder()
                .full_name("Test Test")
                .email("test@example.com")
                .username("testtest")
                .build();
    }

    private UserEntity createUserEntity(int id) {
        return UserEntity.builder()
                .id(id)
                .fullName("Test Test")
                .email("test@example.com")
                .username("testtest")
                .postEntities(new ArrayList<>())
                .build();
    }

    @Test
    void createUser_returnsSavedUser() {
        User user = createUserObject();

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        assertEquals("Test Test", result.getFull_name());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testtest", result.getUsername());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void getAllUsers_returnsAllUsers() {
        UserEntity entity1 = createUserEntity(1);
        UserEntity entity2 = createUserEntity(2);

        when(userRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Test Test", result.get(0).getFull_name());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_returnsUserWhenFound() {
        UserEntity expectedUserResult = createUserEntity(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(expectedUserResult));

        User actualUserResult = userService.getUserById(1);

        assertEquals("Test Test", actualUserResult.getFull_name());
        assertEquals("testtest", actualUserResult.getUsername());
    }

    @Test
    void getUserById_throwsExceptionWhenNotFound() {
        // Stubbing
        when(userRepository.findById(99))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(99)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateUser_updatesExistingUser() {
        UserEntity entity = createUserEntity(1);

        User updatedUser = User.builder()
                .full_name("Test Test2")
                .email("test2@example.com")
                .username("testtest2")
                .build();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(entity));

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1, updatedUser);

        assertEquals("Test Test2", result.getFull_name());
        assertEquals("test2@example.com", result.getEmail());
        assertEquals("testtest2", result.getUsername());
    }

    @Test
    void updateUser_throwsExceptionWhenNotFound() {
        when(userRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(99, createUserObject())
        );

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void deleteUser_deletesExistingUser() {
        when(userRepository.existsById(1)).thenReturn(true);
        userService.deleteUser(1);

        verify(userRepository).existsById(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_throwsExceptionWhenNotFound() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(99)
        );

        verify(userRepository, never()).deleteById(99);
    }

    @Test
    void getUserPosts_returnsEmptyListWhenUserHasNoPosts() {
        UserEntity entity = createUserEntity(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(entity));

        List<org.example.blogplatformapi.model.Post> result =
                userService.getUserPosts(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserPosts_throwsExceptionWhenUserNotFound() {
        when(userRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.getUserPosts(99)
        );
    }
}
