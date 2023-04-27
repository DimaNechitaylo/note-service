package com.test.noteservice.repository;

import com.test.noteservice.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, Long> {
    Optional<List<Like>> findByUserId(String userId);
    Optional<List<Like>> findByNoteId(String noteId);
    Optional<Like> findByUserIdAndNoteId(String userId, String noteId);
    void deleteByUserIdAndNoteId(Long userId, Long noteId);
    boolean existsByUserIdAndNoteId(Long userId, Long noteId);
}
