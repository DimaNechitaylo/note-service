package com.test.noteservice.service;

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

import java.util.List;
import java.util.Optional;

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
    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByCreatedAtDesc()
                .orElseThrow(() -> new NoteNotFoundException("Notes have not been found"));
    }

    @Override
    public Note createNote(String content, String userId) {
        User user = userRepository.findById(userId).orElse(null);
        Note note = Note.builder()
                .content(content)
                .user(user)
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
    public void addLikeToNoteById(String noteId, String userId) throws LikeException {
        if (likeRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new LikeException(); //TODO
        }
        Like like = new Like(userId, noteId);
        likeRepository.save(like);
    }

    @Override
    public void removeLikeFromNoteById(String noteId, String userId) throws UnlikeException {
        if (!likeRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UnlikeException(); //TODO
        }
        likeRepository.deleteByUserIdAndNoteId(userId, noteId);
    }
}
