package com.test.noteservice.service;
import com.test.noteservice.model.Note;

import java.util.List;

public interface NoteService {
    Note getNoteById(Long id);
    List<Note> getAllNotes();
    Note createNote(String content, String userId);
    Note updateNote(Long noteId, String content);
    void deleteNoteById(Long noteId);
    void addLikeToNoteById(Long noteId, Long userId);
    void removeLikeFromNoteById(Long noteId, Long userId);
}
