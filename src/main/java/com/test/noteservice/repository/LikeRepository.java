package com.test.noteservice.repository;

import com.test.noteservice.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    Optional<List<Like>> findByUserId(String userId);
    Optional<List<Like>> findByNoteId(String noteId);
    Optional<Like> findByUserIdAndNoteId(String userId, String noteId);
    void deleteByUserIdAndNoteId(String userId, String noteId);
    boolean existsByUserIdAndNoteId(String userId, String noteId);
    Long countByNoteId(String noteId);
}
