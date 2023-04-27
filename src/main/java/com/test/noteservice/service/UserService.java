package com.test.noteservice.service;

import com.test.noteservice.exception.UsernameExistsException;
import com.test.noteservice.model.User;

import java.util.List;

public interface UserService {
    User getUserById(String id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User createUser(User user) throws UsernameExistsException;
    User updateUser(String id, User user);
    void deleteUser(String id);
}

