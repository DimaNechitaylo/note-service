package com.test.noteservice.repository;

import com.test.noteservice.model.Note;
import com.test.noteservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends MongoRepository<Note, Long> {
    Optional<List<Note>> findAllByOrderByCreatedAtDesc();
    Optional<List<Note>> findAllByUserOrderByCreatedAtDesc(User user);
    Optional<Note> findByIdAndUser(String id, User user);
}
