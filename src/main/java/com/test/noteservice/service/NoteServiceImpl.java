package com.test.noteservice.service;

import com.test.noteservice.dto.NoteDto;
import com.test.noteservice.exception.LikeException;
import com.test.noteservice.exception.NoteNotFoundException;
import com.test.noteservice.exception.UnlikeException;
import com.test.noteservice.exception.UserNotFoundException;
import com.test.noteservice.model.Like;
import com.test.noteservice.model.Note;
import com.test.noteservice.model.User;
import com.test.noteservice.repository.LikeRepository;
import com.test.noteservice.repository.NoteRepository;
import com.test.noteservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public Note getNoteById(String id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));
    }

    @Override
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAllByOrderByCreatedAtDesc()
                .orElseThrow(() -> new NoteNotFoundException("Notes have not been found"))
                .stream()
                .map(note -> {
                    return NoteDto.builder()
                            .id(note.getId())
                            .content(note.getContent())
                            .username(note.getUser() != null ? note.getUser().getUsername() : null)
                            .likesCount(getLikesCount(note.getId()))
                            .createdAt(note.getCreatedAt())
                            .build();

                })
                .collect(Collectors.toList());
    }

    @Override
    public Note createNote(String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        Note note = Note.builder()
                .content(content)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        return noteRepository.save(note);
    }

    @Override
    public Note createNote(String content) {
        Note note = Note.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return noteRepository.save(note);
    }

    @Override
    public Note updateNote(String noteId, String content) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("User not found with id " + noteId));
        note.setContent(content);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNoteById(String noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new NoteNotFoundException("Note not found with id " + noteId);
        }
        noteRepository.deleteById(noteId);
    }

    @Override
    public void addLikeToNoteById(String noteId, String username) throws LikeException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        String userId = user.getId();
        if (likeRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new LikeException("Like from " + userId + " user to " + noteId + " note already exist");
        }
        Like like = new Like(userId, noteId);
        likeRepository.save(like);
    }

    @Override
    public void removeLikeFromNoteById(String noteId, String username) throws UnlikeException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        String userId = user.getId();
        if (!likeRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UnlikeException("User " + userId + " does not have like on " + noteId + " note");
        }
        likeRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

    private Long getLikesCount(String noteId) {
        return likeRepository.countByNoteId(noteId);
    }
}
