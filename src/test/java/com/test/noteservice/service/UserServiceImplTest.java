package com.test.noteservice.service;

import com.test.noteservice.exception.UserNotFoundException;
import com.test.noteservice.exception.UsernameExistsException;
import com.test.noteservice.model.User;
import com.test.noteservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("1")
                .username("user")
                .password("password")
                .build();
        ;
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User actualUser = userService.getUserById(user.getId());

        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test
    void testGetUserByIdThrowsException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void testGetUserByUsernameThrowsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername(user.getUsername()));
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> actualUserList = userService.getAllUsers();

        assertNotNull(actualUserList);
        assertEquals(1, actualUserList.size());
        assertEquals(user, actualUserList.get(0));
    }

    @Test
    void testCreateUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User actualUser = null;
        try {
            actualUser = userService.createUser(user);
        } catch (UsernameExistsException e) {
            fail("An exception was thrown: " + e.getMessage());
        }

        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test
    void testCreateUserThrowsException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UsernameExistsException.class, () -> userService.createUser(user));
    }

    @Test
    public void testUpdateUserSuccess() {
        User newUser = User.builder()
                .id("1")
                .username("newUser")
                .password("newPassword")
                .build();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User updatedUser = userService.updateUser("1", newUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User capturedUser = captor.getValue();

        assertEquals("1", capturedUser.getId());
        assertEquals("newUser", capturedUser.getUsername());
        assertEquals("newPassword", capturedUser.getPassword());

        assertEquals("1", updatedUser.getId());
        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
    }

    @Test
    public void testUpdateUserNotFound() {
        User newUser = User.builder()
                .id("2")
                .username("newUser")
                .password("newPassword")
                .build();

        when(userRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser("2", newUser));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_UserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        userService.deleteUser(user.getId());
    }

    @Test
    void deleteUser_UserNotFound() {
        String userId = "1";
        when(userRepository.existsById(anyString())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }
}