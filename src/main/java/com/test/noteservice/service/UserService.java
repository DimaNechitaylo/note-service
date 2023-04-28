package com.test.noteservice.service;

import com.test.noteservice.exception.UsernameExistsException;
import com.test.noteservice.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(String id);
    UserDetails loadUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user) throws UsernameExistsException;
    User updateUser(String id, User user);
    void deleteUser(String id);
}

